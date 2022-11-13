package com.data.annotation;

import com.data.dbConfig.DataSourceEnum;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Ds.
 */
@Transactional
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD, ElementType.TYPE
})
@Documented
public @interface DS {
    /**
     * Value data source enum.
     * @return the data source enum
     */
    DataSourceEnum value() default DataSourceEnum.master;

    /**
     * Data source name data source enum.
     * @return the data source enum
     */
    DataSourceEnum dataSourceName() default DataSourceEnum.master;

    /**
     * Target ds data source enum.
     * @return the data source enum
     */
    @AliasFor("dataSourceName")
    DataSourceEnum targetDs() default DataSourceEnum.master;
}
