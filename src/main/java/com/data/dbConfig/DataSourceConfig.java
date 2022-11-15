package com.data.dbConfig;

import com.data.dbConfig.model.DynamicDatasourceProperty;
import com.data.dbConfig.model.DynamicDatasourcePropertyConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type Data source config.
 */
@MapperScan(basePackages = "${spring.datasource.mapper-dao-path}", sqlSessionFactoryRef = "sqlSessionFactory")
@Configuration
public class DataSourceConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    private static Map<String, DynamicDatasourceProperty> dsMap;

    private final DynamicDatasourcePropertyConfig datasourceConfig;

    /**
     * Instantiates a new Data source config.
     * @param datasourceConfig the datasource config
     */
    public DataSourceConfig(DynamicDatasourcePropertyConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    /**
     * After properties set.
     */
    @Override
    public void afterPropertiesSet() {
        dsMap = datasourceConfig.getDs().stream().collect(Collectors.toConcurrentMap(DynamicDatasourceProperty::getKey, key -> key));
    }

    /**
     * Data source data source.
     * @return the data source
     */
    @Primary
    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDatasource = new DynamicDataSource();
        LOGGER.info("init dynamicDatasource size:{}", dsMap.size());
        Map<Object, Object> targetDataSourceMap = new HashMap<>(dsMap.size());
        dsMap.forEach((String key, DynamicDatasourceProperty switchDataSourceConfig) -> {
            DataSource dataSource;
            if (StringUtils.endsWithIgnoreCase(DataSourceEnum.master.name(), key)) {
                dataSource = DynamicDatasourceUtil.createDatasource(datasourceConfig, switchDataSourceConfig);
                // 默认数据源
                dynamicDatasource.setDefaultTargetDataSource(dataSource);
            }
            dataSource = DynamicDatasourceUtil.createDatasource(datasourceConfig, switchDataSourceConfig);
            targetDataSourceMap.put(key, dataSource);
        });
        // 目标数据源
        dynamicDatasource.setTargetDataSources(targetDataSourceMap);
        return dynamicDatasource;
    }


    /**
     * 把动态数据源放到SqlSessionFactory
     * 同时配置扫描的mapping.xml
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Primary
    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dynamicDataSource());
        // 添加一行如下代码,解决setTypeAliasesPackage别名设置不起作用问题
        // sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        // mybatis的xml文件中需要写类的全限定名,较繁琐,可以配置自动扫描包路径给类配置别名(可逗号分开)
        // sqlSessionFactoryBean.setTypeAliasesPackage("com.data.model,com.data.entity");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(datasourceConfig.getMapperLocationPath()));
        // 添加mybatis-config路径
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource mybatisConfigXml = resourceResolver.getResource("classpath:mybatis-config.xml");
        if (Objects.nonNull(mybatisConfigXml)) {
            //设置mybatis的主配置文件
            sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);
        }
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * Master transaction manager data source transaction manager.
     * @param dataSource the data source
     * @return the data source transaction manager
     */
    @Primary
    @Bean(name = "switchTransactionManager")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("dynamicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}