## 头部登录状态
### shiro标签的引用
由于shiro标签不是html的原生标签，所有我们需要先引入一个额外的依赖，shiro的标签库(thymeleaf的拓展标签)。
```
	<dependency>
			<groupId>com.github.theborakompanioni</groupId>
			<artifactId>thymeleaf-extras-shiro</artifactId>
			<version>2.0.0</version>
		</dependency>
```
依赖添加好之后，然后，我们需要在`com.fly.config.ShiroConfig` 中初始化一下，注入对应的Bean, 页面才能渲染出来

```
    //用于thymeleaf模板使用shiro标签,shiro方言标签
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
```
然后在需要使用shiro标签的html 文件的头部添加

```
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
```
添加好之后，就可以使用` <shiro:user></shiro:user>` 将要权限控制的内容包起来，当然shiro 标签还有很多
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181219191438798.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)
### 用户信息存到session中
用户登录成功之后需要将用户的信息保存的session中。我们只需要在用户认证的方法中`com.fly.shiro.OAuth2Realm` 类的`doGetAuthenticationInfo` 方法中加上如下语句：
```
//        将登陆信息放在session
        SecurityUtils.getSubject().getSession().setAttribute("profile",profile);
```
经过如下如上设置我们就实现了登录头部状态的控制
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181219192629312.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)

## 完善个人信息
### 用户中心
用户中心主要就两个，我发的贴和我收藏的贴
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181219193815705.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)
我发的帖子，`在这里插入代码片com.homework.controller.CenterController#center` 查询条件只有用户id
```
        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().eq("user_id", getProfileId())
                .orderByDesc("created");
        IPage<Map<String, Object>> pageData = postService.pageMaps(page, wrapper);
        request.setAttribute("pageData", pageData);

```
我的收藏

```
 IPage<Map<String, Object>> pageData = userCollectionService.
                pageMaps(page, new QueryWrapper<UserCollection>()
                        .eq("user_id", getProfileId()).orderByDesc("created"));

        postService.join(pageData, "post_id");
        request.setAttribute("pageData", pageData);
```
### 基本设置

 1. tab 切换回显的问题，一个页面有多个tab，如何让在选中tab 之后刷新不丢失原来的tab选中选项呢？答案是在url 后面加上#，这相当于标签的效果。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20181219195742292.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)
当前tab页标签定义：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181219201604844.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)

在`static/mods/user.js` 有如下语句：

```
  //显示当前tab
  if(location.hash){
    element.tabChange('user', location.hash.replace(/^#/, ''));
  }

  element.on('tab(user)', function(){
    var othis = $(this), layid = othis.attr('lay-id');
    if(layid){
      location.hash = layid;
    }
  });
```
我们在`templates/common/static.html` 放入了如下代码，并修改下信息

```
<script th:inline="javascript" th:if="${session.profile != null}">
    layui.cache.page = '';
    layui.cache.user = {
        username: [[${session.profile.username}]]
        ,uid: [[${session.profile.id}]]
        ,avatar: [[${session.profile.avatar}]]
        ,experience: 0
        ,sex: [[${session.profile.gender}]]
    };
    layui.config({
        version: "3.0.0",
        base: '/mods/' //这里实际使用时，建议改成绝对路径
    }).extend({
        fly: 'index'
    }).use('fly');
</script>
```
通过上面代码，我们把初始化layui的部分js代码，页面中很多class或id 的div 就拥有了特定的监听或其他。其中就报货截取url 获取#后面的标签用于tab 回显功能，还有头像的上传功能封装等。
加上了上面代码之后你会发现经常会有个异常的弹框，那是浏览器控制台发现去访问`/message/nums`

 4. 头像
 5. 密码
