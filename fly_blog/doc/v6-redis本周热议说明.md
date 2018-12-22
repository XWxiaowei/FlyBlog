
本周热议，本周发表并且评论最多的文章排行，如果直接查询数据库的话很快就可以实现，只需要限定一下文章创建时间，然后根据评论数量倒叙取前几篇即可搞定。

但这里我们使用redis来完成。之前上课时候我们说过，排行榜功能，我们可以使用redis的有序集合zset来完成。现在我们就这个数据结构来完成本周热议的功能。

在编码之前，我们需要先来回顾一下zset的几个基本命令。

**zrange key start stop [WITHSCORES]** 
withscores代表的是否显示顺序号  start和stop代表所在的位置的索引。可以这样理解：将集合元素依照顺序值升序排序再输出，start和stop限制遍历的限制范围
**zincrby key increment member**
为有序集 key 的成员 member 的 score 值加上增量 increment 。
**ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]**
计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
默认情况下，结果集中某个成员的 score 值是所有给定集下该成员 score 值之 和 
## 实现步骤

 1. 查库获取最近7天的所有评论数量大于 0文章
 2. 把文章的评论数量作为有序集合的分数，文章id作为id存储到zset中
 3. 缓存文章到set中，评论数量作为排行标准
 4. 设置有效期为7天，因为超过了7天也就失去了时效性
## 具体代码实现
 com.fly.service.impl.PostServiceImpl#initIndexWeekRank
 

```
    @Override
    public void initIndexWeekRank() {
//        查库获取最近7天的所有文章
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                .ge("created", DateUtil.offsetDay(new Date(), -7).toJdkDate())
                .gt("comment_count", 0)
                .select("id, title, user_id, comment_count, view_count, created"));
//        然后把文章的评论数量作为有序集合的分数，文章id作为ID存储到zset中。
        for (Post post : last7DayPosts) {
            String key = "day_rank" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);
//            设置有效期,7天之内有效
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

//            缓存文章到set中，评论数量作为排行标准
            redisUtil.zSet(key, post.getId(), post.getCommentCount());
            //设置有效期
            redisUtil.expire(key, expireTime);
//            缓存文章基本信息（hash结构）
            this.hashCachePostIdAndTitle(post);

        }

//        7天阅读相加
        this.zUnionAndStroreLast7DaysForWeekRand();

    }


```
因为要显示文章的标题等基本信息
```
  /**
     * hash结构缓存文章标题和id
     *
     * @param post
     */
    private void hashCachePostIdAndTitle(Post post) {
        boolean isExist = redisUtil.hasKey("rank_post_" + post.getId());
        if (!isExist) {
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

//            缓存文章基本信息
            redisUtil.hset("rank_post_" + post.getId(), "post:id", post.getId(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
        }
    }

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    public void zUnionAndStroreLast7DaysForWeekRand() {
        String prifix = "day_rank";

        List<String> keys = new ArrayList<>();
        String key = prifix + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);

        for (int i = -7; i < 0; i++) {
            Date date = DateUtil.offsetDay(new Date(), i).toJdkDate();
            keys.add(prifix + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        }

        redisUtil.zUnionAndStore(key, keys, "last_week_rank");

    }
```
我们在项目启动之时初始化，代码如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181222224846871.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)
##  增加评论数量
用户发表评论之后应该要数据库中的comment_count 加1，也要让缓存中数量加1，这样才能保证数据的一致性，在com.fly.controller.PostController#commentAdd 中
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181222225459173.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)


```
    @Override
    public void incrZsetValueAndUnionForLastWeekRank(Long postId) {
        String dayRank = "day_rank" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
//        文章阅读加一
        redisUtil.zIncrementScore(dayRank, postId, 1);
        this.hashCachePostIdAndTitle(this.getById(postId));

//      重新union最近7天
        this.zUnionAndStroreLast7DaysForWeekRand();
    }
```

其实逻辑也和初始化差不多，首先给文章数量加一。集合名称是对应当天的。比如今天是12月21，对应的key就是day_rank:20181221。在这个上面给对应的文章id加一。这样每天都有评论这篇文章的话，我们再做交集处理，把每天的评论数量都加起来。得到的就是总的评论数量了。
前端引用的代码
common/templates.html
```
<!--本周热议-->
<div th:fragment="weekPopular">
    <dl class="fly-panel fly-list-one" id="post-hots">
        <dt class="fly-panel-title">本周热议</dt>
        <script id="hostDd" type="text/html">
            {{#  layui.each(d.list, function(index, item){ }}
            <dd>
                <a href="/post/{{item.id}}">{{item.title}}</a>
                <span><i class="iconfont icon-pinglun1"></i>{{item.comment_count}}</span>
            </dd>
            {{#  }); }}
            {{#  if(d.list.length === 0){ }}
            <div class="fly-none">没有相关数据</div>
            {{#  } }}
        </script>
    </dl>
    <script>
        layui.use(['laytpl'], function () {
            var laytpl = layui.laytpl;

            var post_hosts = document.getElementById("post-hots");
            var tpl = document.getElementById("hostDd").innerHTML;
            var data = {};
            $.ajax({
                url: "/post/hosts",
                async: false,
                success: function (res) {
                    if (res.code == 0) {
                        data.list = res.data;
                    }
                }
            });
            laytpl(tpl).render(data, function (html) {
                post_hosts.innerHTML += html;

            });

        });

    </script>
</div>

```