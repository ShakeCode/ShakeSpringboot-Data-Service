package com.data.dbConfig.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Dynamic datasource property.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.ds")
public class DynamicDatasourceProperty {
    private String key;

    private String type;

    private String url;

    private String driverClassName;

    private String username;

    private String password;
}
