package com.data.dbConfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.data.dbConfig.model.DynamicDatasourceProperty;
import com.data.dbConfig.model.DynamicDatasourcePropertyConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Dynamic datasource util.
 */
@Slf4j
public class DynamicDatasourceUtil {
    /**
     * The constant DATASOURCES.
     */
    public static final Map<String, DataSource> DATASOURCES = new ConcurrentHashMap<>();

    /**
     * Create datasource.
     * @param datasourceConfig the datasource config
     * @param property         the property
     * @return the data source
     */
    public synchronized static DataSource createDatasource(DynamicDatasourcePropertyConfig datasourceConfig, DynamicDatasourceProperty property) {
        if (DynamicDatasourceUtil.DATASOURCES.containsKey(property.getKey())) {
            return DATASOURCES.get(property.getKey());
        }
        DataSource dataSource;
        if (Objects.nonNull(property.getType()) && ("com.zaxxer.hikari.HikariDataSource".equals(property.getType()) || "hikari".equals(property.getType()))) {
            dataSource = createHikariDataSource(datasourceConfig, property);
        } else if (Objects.nonNull(property.getType()) && ("com.alibaba.druid.pool.DruidDataSource".equals(property.getType()) || "druid".equals(property.getType()))) {
            dataSource = createDruidDataSource(datasourceConfig, property);
        } else {
            dataSource = createDefaultDataSource(property);
        }
        log.info("创建数据源: key: {}, type: {}", property.getKey(), property.getType());
        DynamicDatasourceUtil.DATASOURCES.putIfAbsent(property.getKey(), dataSource);
        return dataSource;
    }

    private static DataSource createDruidDataSource(DynamicDatasourcePropertyConfig datasourceConfig, DynamicDatasourceProperty property) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(property.getDriverClassName());
        druidDataSource.setUrl(property.getUrl());
        druidDataSource.setUsername(property.getUsername());
        druidDataSource.setPassword(property.getPassword());
        if (Objects.nonNull(datasourceConfig.getMaxActive())) {
            // 最大连接池数量
            druidDataSource.setMaxActive(datasourceConfig.getMaxActive());
        }
        if (Objects.nonNull(datasourceConfig.getInitialSize())) {
            // 初始连接数
            druidDataSource.setInitialSize(datasourceConfig.getInitialSize());
        }
        if (Objects.nonNull(datasourceConfig.getMinIdle())) {
            // 最小连接池数量
            druidDataSource.setMinIdle(datasourceConfig.getMinIdle());
        }
        if (Objects.nonNull(datasourceConfig.getPoolName())) {
            druidDataSource.setName(datasourceConfig.getPoolName());
        }
        if (Objects.nonNull(datasourceConfig.getMaxWait())) {
            // 获取连接等待超时的时间
            druidDataSource.setMaxWait(datasourceConfig.getMaxWait());
        }
        if (Objects.nonNull(datasourceConfig.getValidationQuery())) {
            // 配置检测连接是否有效
            druidDataSource.setValidationQuery(datasourceConfig.getValidationQuery());
        }
        // 检测间隔时间，检测需要关闭的空闲连接，单位毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(datasourceConfig.getTimeBetweenEvictionRunsMillis());
        // 一个连接在连接池中最小的生存时间，单位毫秒
        druidDataSource.setMinEvictableIdleTimeMillis(datasourceConfig.getMinEvictableIdleTimeMillis());
        // 一个连接在连接池中最大的生存时间，单位毫秒
        druidDataSource.setMaxEvictableIdleTimeMillis(datasourceConfig.getMaxEvictableIdleTimeMillis());
        // 如果为true（默认为false），当应用向连接池申请连接时，连接池会判断这条连接是否是可用的
        druidDataSource.setTestOnBorrow(datasourceConfig.isTestOnBorrow());
        // 连接返回检测
        druidDataSource.setTestOnReturn(datasourceConfig.isTestOnReturn());
        // 失效连接检测
        druidDataSource.setTestWhileIdle(datasourceConfig.isTestWhileIdle());

        if (Objects.nonNull(datasourceConfig.getMaxOpenPreparedStatements())) {
            druidDataSource.setMaxOpenPreparedStatements(datasourceConfig.getMaxOpenPreparedStatements());
        }

        if (Objects.nonNull(datasourceConfig.isPoolPreparedStatements())) {
            druidDataSource.setPoolPreparedStatements(datasourceConfig.isPoolPreparedStatements());
        }

        if (Objects.nonNull(datasourceConfig.getConnectionErrorRetryAttempts())) {
            druidDataSource.setConnectionErrorRetryAttempts(datasourceConfig.getConnectionErrorRetryAttempts());
        }

        if (Objects.nonNull(datasourceConfig.isBreakAfterAcquireFailure())) {
            druidDataSource.setBreakAfterAcquireFailure(datasourceConfig.isBreakAfterAcquireFailure());
        }

        if (Objects.nonNull(datasourceConfig.getTimeBetweenConnectErrorMillis())) {
            druidDataSource.setTimeBetweenConnectErrorMillis(datasourceConfig.getTimeBetweenConnectErrorMillis());
        }

        if (Objects.nonNull(datasourceConfig.isRemoveAbandoned())) {
            druidDataSource.setRemoveAbandoned(datasourceConfig.isRemoveAbandoned());
        }

        if (Objects.nonNull(datasourceConfig.getRemoveAbandonedTimeout())) {
            druidDataSource.setRemoveAbandonedTimeout(datasourceConfig.getRemoveAbandonedTimeout());
        }

        if (Objects.nonNull(datasourceConfig.getTransactionQueryTimeout())) {
            druidDataSource.setTransactionQueryTimeout(datasourceConfig.getTransactionQueryTimeout());
        }

        if (Objects.nonNull(datasourceConfig.getFilters())) {
            try {
                druidDataSource.setFilters(datasourceConfig.getFilters());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (Objects.nonNull(datasourceConfig.getConnectionProperties())) {
            Properties properties = new Properties();
            // druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
            String[] propertyArray = datasourceConfig.getConnectionProperties().split(";");
            for (int i = 0; i < propertyArray.length; i++) {
                String[] targetPropertyArray = propertyArray[i].split("=");
                properties.put("" + targetPropertyArray[0], targetPropertyArray[1]);
            }
            druidDataSource.setConnectProperties(properties);
        }
        return druidDataSource;
    }

    private static DataSource createDefaultDataSource(DynamicDatasourceProperty property) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(property.getDriverClassName());
        dataSource.setUrl(property.getUrl());
        dataSource.setUsername(property.getUsername());
        dataSource.setPassword(property.getPassword());
        return dataSource;
    }

    private static HikariDataSource createHikariDataSource(DynamicDatasourcePropertyConfig datasourceConfig, DynamicDatasourceProperty property) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(property.getDriverClassName());
        dataSource.setJdbcUrl(property.getUrl());
        dataSource.setUsername(property.getUsername());
        dataSource.setPassword(property.getPassword());
        if (Objects.nonNull(datasourceConfig.getMaximumPoolSize())) {
            dataSource.setMaximumPoolSize(datasourceConfig.getMaximumPoolSize());
        }
        if (Objects.nonNull(datasourceConfig.getMinIdle())) {
            dataSource.setMinimumIdle(datasourceConfig.getMinIdle());
        }
        if (Objects.nonNull(datasourceConfig.getPoolName())) {
            dataSource.setPoolName(datasourceConfig.getPoolName());
        }
        if (Objects.nonNull(datasourceConfig.getIdleTimeout())) {
            dataSource.setIdleTimeout(datasourceConfig.getIdleTimeout());
        }
        if (Objects.nonNull(datasourceConfig.getMaxLifetime())) {
            dataSource.setMaxLifetime(datasourceConfig.getMaxLifetime());
        }
        if (Objects.nonNull(datasourceConfig.getConnectionTimeout())) {
            dataSource.setConnectionTimeout(datasourceConfig.getConnectionTimeout());
        }
        return dataSource;
    }
}
