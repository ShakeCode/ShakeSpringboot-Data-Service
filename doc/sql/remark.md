netstat -ano | findstr 8019
TCP    0.0.0.0:8019           0.0.0.0:0              LISTENING       4196
TCP    [::]:8019              [::]:0                 LISTENING       4196

taskkill /f /pid 4196


### springboot 2.0以上目前支持的数据源有以下三种
com.zaxxer.hikari.HikariDataSource (Spring Boot 2.0 以上,默认使用此数据源)

org.apache.tomcat.jdbc.pool.DataSource

org.apache.commons.dbcp2.BasicDataSource
