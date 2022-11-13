package com.data.dbConfig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Master data source config.
 */
@Configuration
@MapperScan(basePackages = MasterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {
    /**
     * The constant PACKAGE.
     */
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.data.dao.master";
    /**
     * The Mapper location.
     */
    static final String MAPPER_LOCATION = "classpath*:mapper/master/*.xml";

    /**
     * Create data source t.
     * @param <T>        the type parameter
     * @param properties the properties
     * @param type       the type
     * @return the t
     */
    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    /**
     * Master data source hikari data source.
     * @return the hikari data source
     */
    @Bean(name = "masterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        DataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        /*HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }*/
        return dataSource;
    }

    /**
     * Master sql session factory sql session factory.
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Bean("masterSqlSessionFactory")
    @Primary
//    @Primary /*此处必须在主数据库的数据源配置上加上@Primary*/
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MasterDataSourceConfig.MAPPER_LOCATION));
        return factoryBean.getObject();
    }

    /**
     * Master transaction manager data source transaction manager.
     * @param dataSource the data source
     * @return the data source transaction manager
     */
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
