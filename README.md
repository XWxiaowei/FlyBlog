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

## 全局异常处理
## 首页处理
