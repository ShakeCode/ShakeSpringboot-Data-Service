netstat -ano | findstr 8019
TCP    0.0.0.0:8019           0.0.0.0:0              LISTENING       4196
TCP    [::]:8019              [::]:0                 LISTENING       4196

taskkill /f /pid 4196


### springboot 2.0以上目前支持的数据源有以下三种
com.zaxxer.hikari.HikariDataSource (Spring Boot 2.0 以上,默认使用此数据源)

org.apache.tomcat.jdbc.pool.DataSource

org.apache.commons.dbcp2.BasicDataSource
                                                 

###
```
easyexcel官方地址:

https://easyexcel.opensource.alibaba.com/index.html

https://github.com/alibaba/easyexcel

```
![img_2.png](img_2.png)

### freemarker
1. https://geek-docs.com/spring-boot/spring-boot-tutorials/spring-boot-freemarker.html
2. 官网: https://freemarker.apache.org/
3. http://freemarker.foofun.cn/

![img_3.png](img_3.png)

### ehcache
1. 官网: https://www.ehcache.org/documentation/
2. 分布式缓存: https://www.cnblogs.com/kidezyq/p/9243878.html
3. https://blog.csdn.net/weixin_30546933/article/details/98150578?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EESLANDING%7Edefault-2-98150578-blog-126363255.pc_relevant_landingrelevant&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EESLANDING%7Edefault-2-98150578-blog-126363255.pc_relevant_landingrelevant&utm_relevant_index=5