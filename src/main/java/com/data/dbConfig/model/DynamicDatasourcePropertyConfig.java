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
    /*DruidDataSource使用*/
}
