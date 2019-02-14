package com.data.dbConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
public class DataSourceConfig {

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
/*    @Bean(name = "dynamicDataSource")
    public AbstractRoutingDataSource  dynamicDataSource(@Qualifier("master") DataSource masterDb,
                                        @Qualifier("custer") DataSource custerDb){
        AbstractRoutingDataSource  dynamicDataSource = new DynamicDataSource();  //继承AbstractRoutingDataSource
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDb);
        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap(5);
        dsMap.put("master", masterDb);
        dsMap.put("custer",custerDb);
        dynamicDataSource.setTargetDataSources(dsMap);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }*/

//    @ConfigurationProperties(prefix = "mybatis")
/*    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource 作为数据源则不能实现切换
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean.getObject();
    }*/

    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     */
  /*  @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }*/
}
