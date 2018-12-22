# FlyBlog
二期90天进阶训练营的课程课后作业，搭建一个blog
## 20181101更新
## 摘要
本期主要完成了集成mybatis plus、lombok，Redis，做好全局异常处理，并且把layui社区的页面集成到项目中，然后就是完成首页的渲染。
## 环境
  
| 框架 |版本   |
|--|--|
| springboot  | 2.0.1.RELEASE |
| JDK  | 1.8 |
| mysql  | 5.6 |
## 项目结构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181101081711918.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)
## 集成MyBatis plus
###  说明
MyBatis Plus 是一个持久层框架，是在MyBatis 之上做的一层封装，通过对Dao层，Servcie层通用代码的封装，极大的简化了开发。
详情参考：https://github.com/baomidou/mybatis-plus
### 步骤一 添加依赖
步骤一：在pom文件中添加 MyBatis Plus 的依赖，使用最新版本
```
<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-boot-starter</artifactId>
			<version>3.0.1</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
```
###  步骤二.  数据库连接配置
步骤二  新建数据`fly_blog`，并在application.yml 文件中添加数据库连接配置。
```
# DataSource Config
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/fly_blog
    username: root
    password: admin

```
### 步骤三. 配置Mapper的扫描路径
在程序的入口类`FlyBlogApplication` 添加`@MapperScan(value = "com.fly.dao")` 注解
```
@SpringBootApplication
@MapperScan(value = "com.fly.dao")
public class FlyBlogApplication {


	public static void main(String[] args) {

		SpringApplication.run(FlyBlogApplication.class, args);
		log.info("系统启动成功");
	}
}
```
至此，MyBatis Plus 就集成完毕，在3.0.1 版本的MyBatis Plus中不需要配置xml的位置。
放下以下两个位置均可
位置一：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181101081456470.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)
位置二
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181101081534693.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ1MzQ4MDg=,size_16,color_FFFFFF,t_70)

MyBatisPlus 还给我们提供了一个特别实用的功能。自动生成代码，它可以自动生成dao,model,mapper,service,controller的代码。
### 自动生成代码配置
生成代码启动类
```
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ResourceBundle;


/**
 * Created by Lucare.Feng on 2017/2/23.
 */
public class MyGenerator {

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {

        //用来获取Mybatis-Plus.properties文件的配置信息
        ResourceBundle rb = ResourceBundle.getBundle("Mybatis-Plus");
        AutoGenerator mpg = new AutoGenerator();
        String systemDir = System.getProperty("user.dir");
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(systemDir + rb.getString("OutputDir"));
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor(rb.getString("author"));

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        String classPrefix = "%s";
        gc.setMapperName(classPrefix+"Mapper");
        gc.setXmlName(classPrefix+"Mapper");
        gc.setServiceName(classPrefix+"Service");
        gc.setServiceImplName(classPrefix+"ServiceImpl");
        gc.setControllerName(classPrefix+"Controller");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
//        dsc.setTypeConvert(new MySqlTypeConvert());
//        dsc.setTypeConvert(new MySqlTypeConvert() {
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(String fieldType) {
//                System.out.println("转换类型：" + fieldType);
//                return super.processTypeConvert(fieldType);
//            }
//        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(rb.getString("userName"));
        dsc.setPassword(rb.getString("passWord"));
        dsc.setUrl(rb.getString("url"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
//        strategy.setTablePrefix(new String[]{"t_"});// 此处可以修改为您的表前缀
//        strategy.setNaming(NamingStrategy.remove_prefix_and_camel);// 表名生成策略
//        strategy.setNaming(NamingStrategy.removePrefixAndCamel());// 表名生成策略
//        strategy.setInclude(new String[]{"shop_create_order_record"}); // 需要生成的表
        strategy.setInclude(rb.getString("tableName").split(",")); // 需要生成的表
//        String[] strings = {"mto_users", "shiro_permission"};
//        strategy.setInclude(strings); // 需要生成的表
//        strategy.setExclude(new String[]{"t_rong_bid"}); // 排除生成的表
        // 字段名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 自定义实体父类
//         strategy.setSuperEntityClass("hello.entity.BaseEntity");
        // 自定义实体，公共字段
//        strategy.setSuperEntityColumns(new String[]{"id"});
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.fcs.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.fcs.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.fcs.demo.TestServiceImpl");
        // 自定义 controller 父类
//         strategy.setSuperControllerClass("com.risk.controller.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(rb.getString("parent"));
        pc.setModuleName("");
        pc.setController("controller");// 这里是控制器包名，默认 web
        pc.setEntity("entity");
        pc.setMapper("dao");
        pc.setXml("mapper");
        pc.setService("service");
        pc.setServiceImpl("service" + ".impl");
        mpg.setPackageInfo(pc);

        // 执行生成
        mpg.execute();
    }

}
```
对应的配置文件MyBatis-Plus.properties

```
#此处为本项目src所在路径（代码生成器输出路径）
OutputDir=/fly_blog/src/main/java
#数据库表名(此处切不可为空，如果为空，则默认读取数据库的所有表名),多个表用","号分割
tableName=user
#生成代码类名类名
#className=MtoLog
#设置作者
author=jay.xiang
#自定义包路径
parent=com.fly
#
#
#正常情况下，下面的代码无需修改！！！！！！！！！！
#
#
#数据库地址
url=jdbc\:mysql\://localhost\:3306/fly_blog?useUnicode\=true&characterEncoding\=utf-8&useSSL\=false
#数据库用户名
userName=root
#数据库密码
passWord=admin
```
至此，MyBatisPlus的配置就完成了。

## 集成lombok
### 添加依赖

```
<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
```
## 集成Redis
### 第一步：导入redis的pom包
`

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>

`
### 第二步：配置redis的连接信息
```
spring:  redis:
    sentinel:
      master: mymaster
      nodes: 

```
第三步：为了让我们存到redis中的数据更容易看懂，我们需要
换一种序列化方式，默认的是采用jdk的序列化方式，这里选用Jackson2JsonRedisSerializer，
只需要重写redisTemplate操作模板的生成方式即可。新建一个config包，放在这个包下。

```

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper());

        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }
}

```
## 全局异常处理
步骤一. 自定义异常类
```
public class MyException extends RuntimeException {
    public MyException(String message) {
        super(message);
    }
}

```
步骤二. 定义全局异常处理，使用`@ControllerAdvice`表示
定义全局控制器异常处理，使用`@ExceptionHandler`表示
针对性异常处理，可对每种异常针对性处理
```
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("------------------>捕获到全局异常", e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURI());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public R jsonErrorHandler(HttpServletRequest req, MyException e) {

        return R.failed(e.getMessage());
    }
}


```
## 数据库设计
sql语句参见： flyblog.sql

以上就是分支1内容[v1-basecode]
----------------------分支二内容[v2-shiro-login]----------------------------------
##集成Shiro
步骤一，引入pom文件
```
	<!--集成shiro-->
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-spring</artifactId>
			<version>1.4.0</version>
		</dependency>


```
Realm: 认证与授权
SecurityManager:Shiro架构的核心，协调内部各个安全组件之间的交互。
步骤二：配置Shiro的SecurityManager核心和过滤器
```
@Slf4j
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);

        log.info("------------->securityManager注入完成");
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {

        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/user/center");
        // 配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");

        Map<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("/login", "anon");
        hashMap.put("/user*", "user");
        hashMap.put("/user/**", "user");
        hashMap.put("/post/**", "user");
        filterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return filterFactoryBean;
    }
}


```
在此处我们重写了认证和授权的Realm，并将Realm配置到SecurityManager中，
然后shiro的过滤器呢，给Shiro配置了登录的url,登录成功url和没有权限提示的url,
有配置了需要拦截的url。

而Realm需要继承AuthorizingRealm并授权和认证方法。
```
@Slf4j
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        注意token.getUsername()是指email!!!
        AccountProfile profile = userService.login(token.getUsername(), String.valueOf(token.getPassword()));
        log.info("-------------------->进入认证步骤");

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(profile, token.getCredentials(), getName());

        return info;
    }
}


```
这时候shiro已经集成到了项目中，启动项目后会打印 "securityManager注入完成"的提示。
然后我们可以使用`SecurityUtils.getSubject()`去操作用户的权限操作了。
## 登录注册
### 登录接口：
```
 @PostMapping("/login")
    @ResponseBody
    public R doLogin(String email, String password, ModelMap model) {
        if (StringUtils.isAnyBlank(email, password)) {
            return R.failed("用户名或密码不能为空");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(email, SecureUtil.md5(password));

        try {

            //尝试登陆，将会调用realm的认证方法
            SecurityUtils.getSubject().login(token);

        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                return R.failed("用户不存在");
            } else if (e instanceof LockedAccountException) {
                return R.failed("用户被禁用");
            } else if (e instanceof IncorrectCredentialsException) {
                return R.failed("密码错误");
            } else {
                return R.failed("用户认证失败");
            }
        }

        return R.ok("登录成功");

    }


```
前端调用：
```
<script>
    layui.use('form',function(){
        var form = layui.form;
//        监听提交
        form.on('submit(*)',function (data) {
            $.post('/login',data.field,function (res) {
                if (res.code==0) {
                    location.href="/user/center";
                }else {
                    layer.msg(res.msg());
                }
            });
            return false;
        });

        });

</script>

```
注册与登录类似：
## 博客分类、分页
### 1. 首页分类显示
由于分类变化较少，所以在项目初始化时候放在上下文猴子那个(ServletContext).
但是这样也有问题，在web端与后台管理端不是同一个项目，或者做了负载均衡的项目，
如果改动了分类信息，因为不同项目不同上下文，所以会出现数据不一致的可能。

现阶段我们先把分类信息存放在上下文中，后期如果涉及到负载均衡或者分离时候，
我们可以通过MQ消息队列的方式来改变上下文内容。

博客分类信息是项目启动之后初始化到上下文，这里涉及到ApplicationRunner接口，
ApplicationRunner接口是在容器启动成功后的最后一步回调（类似于开机自启）。
```
package org.springframework.boot;

@FunctionalInterface
public interface ApplicationRunner {
    void run(ApplicationArguments var1) throws Exception;
}

```
我们需要重写ApplicationRunner类的run方法，因为SpringBoot在启动完成之后
会调用这个run方法。所以我们只需要把博客分类的逻辑在run方法里面实现就行。

另外，因为需要用到上下文，所以我们也实现ServletContextAware方法，重写
serServletContext方法，把serveltContext注入
具体代码如下：
```
@Slf4j
@Order(100)
@Component
public class ContextStartup implements ApplicationRunner,ServletContextAware {

    private ServletContext servletContext;

    @Autowired
    CategoryService categoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        servletContext.setAttribute("categorys", categoryService.list(null));

        log.info("ContextStartup------------>加载categorys");

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}


```
### 分类详情与分页

--------------------------以上代码v4-category--------------------------------
参见 fly_blog/src/doc/v5-collection-center说明.md

