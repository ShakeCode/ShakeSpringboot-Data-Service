package com.data.sql.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class InterceptorConfig {
    @Autowired
    private MybatisSqlInterceptor interceptor;

    public InterceptorConfig() {
    }

    @Bean
    public Interceptor custerSqlInterceptor(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        Properties properties = new Properties();
        this.interceptor.setProperties(properties);
        sqlSessionFactory.getConfiguration().addInterceptor(this.interceptor);
        return this.interceptor;
    }
}