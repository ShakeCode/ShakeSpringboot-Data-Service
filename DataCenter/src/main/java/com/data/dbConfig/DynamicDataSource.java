package com.data.dbConfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//动态数据源路由管理数据源

public class DynamicDataSource extends AbstractRoutingDataSource {

    public  static ConcurrentHashMap<Object,Object> targetDataSourceMap = new ConcurrentHashMap<>();


  /*  public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }*/


  /*  @Override
    protected DataSource determineTargetDataSource() {
        // 根据数据库选择方案，拿到要访问的数据库
        Object dataSourceName = determineCurrentLookupKey();
        // 根据数据库名字，从已创建的数据库中获取要访问的数据库
        DataSource dataSource = (DataSource) targetDataSourceMap.get(dataSourceName);
        return dataSource;
    }*/

    @Override
    protected Object determineCurrentLookupKey() {
//        log.debug("数据源为{}", DataSourceContextHolder.getDB());
        return DataSourceContextHolder.getDB();   //返回数据库名称
    }

  /*  @Override
    public void afterPropertiesSet() {
        determineTargetDataSource();
    }*/
}
