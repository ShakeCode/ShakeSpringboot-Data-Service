package com.data.dbConfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Dynamic multiple data source.
 */
@ConditionalOnProperty(name = "spring.datasource.switch.enabled", havingValue = "true")
@Configuration
public class DynamicMultipleDataSource extends AbstractRoutingDataSource {
    private final YmlMultipleDataSourceProvider ymlMultipleDataSourceProvider;

    /**
     * Instantiates a new Dynamic multiple data source.
     * @param provider the provider
     */
    public DynamicMultipleDataSource(YmlMultipleDataSourceProvider provider) {
        this.ymlMultipleDataSourceProvider = provider;
        this.initTargetDataSources();
    }

    private void initTargetDataSources() {
        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(ymlMultipleDataSourceProvider.loadDataSource());
        // 目标数据源集合
        super.setTargetDataSources(targetDataSources);
        // 默认数据源
        super.setDefaultTargetDataSource(targetDataSources.get(DataSourceEnum.master.name()));
        super.afterPropertiesSet();
    }

    /**
     * Determine current lookup key object.
     * @return the object
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 获取当前数据源标识
        return DataSourceContextHolder.getDB();
    }
}
