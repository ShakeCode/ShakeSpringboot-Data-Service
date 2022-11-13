package com.data.dbConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Dynamic data source.
 */
@Configuration
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    /**
     * The constant targetDataSourceMap.
     */
    public static ConcurrentHashMap<Object, Object> targetDataSourceMap = new ConcurrentHashMap<>();

    /**
     * Determine current lookup key object.
     * @return the object
     */
    @Override
    protected Object determineCurrentLookupKey() {
        LOGGER.debug("数据源为{}", DataSourceContextHolder.getDB());
        //返回数据库名称
        return DataSourceContextHolder.getDB();
    }

}
