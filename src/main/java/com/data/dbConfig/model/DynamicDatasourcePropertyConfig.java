package com.data.dbConfig.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Dynamic datasource property config.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DynamicDatasourcePropertyConfig {
    private String mapperDaoPath;

    private String mapperLocationPath;

    private List<DynamicDatasourceProperty> ds = new ArrayList<>();

    /*HikariDataSource使用*/
    private String poolName;

    private Integer maximumPoolSize;

    private Integer idleTimeout;

    private Long maxLifetime;

    private Long connectionTimeout;
    /*HikariDataSource使用*/

    /*DruidDataSource使用*/
    private Integer initialSize;

    private Integer minIdle;

    private Integer maxActive;

    private Integer maxWait;

    private Integer timeBetweenEvictionRunsMillis;

    private Integer minEvictableIdleTimeMillis;

    private Integer maxEvictableIdleTimeMillis;

    private String validationQuery;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean testWhileIdle;

    private boolean poolPreparedStatements;

    private Integer maxOpenPreparedStatements;

    private Integer connectionErrorRetryAttempts;

    private boolean breakAfterAcquireFailure;

    private Integer timeBetweenConnectErrorMillis;

    private boolean removeAbandoned;

    private Integer removeAbandonedTimeout;

    private Integer transactionQueryTimeout;

    // 配置监控统计拦截的filters,去掉后监控界面sql无法统计,'wall'用于防火墙
    private String filters;

    // 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    private String connectionProperties;
    /*DruidDataSource使用*/
}
