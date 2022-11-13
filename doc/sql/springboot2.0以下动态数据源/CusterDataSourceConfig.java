package com.data.dbConfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = CusterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "custerSqlSessionFactory")
public class CusterDataSourceConfig {

    @Value("${spring.datasource.custer.url}")
    private String url;

    @Value("${spring.datasource.custer.username}")
    private String username;

    @Value("${spring.datasource.custer.password}")
    private String password;

    // 精确到 master 目录，以便跟其他数据源隔离
    public static final String PACKAGE = "com.data.dao.custer";

    public static final String MAPPERXML_LOCATION = "classpath*:mapper/custer/*.xml";

    @Bean(name = "custerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.custer")
    public DataSource custerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("custerSqlSessionFactory")
    public SqlSessionFactory custerSqlSessionFactory(@Qualifier("custerDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        /*加载所有的mapper.xml映射文件*/
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(CusterDataSourceConfig.MAPPERXML_LOCATION));
        return factoryBean.getObject();
    }

    @Bean(name = "custerTransactionManager")
    public DataSourceTransactionManager custerTransactionManager(@Qualifier("custerDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
