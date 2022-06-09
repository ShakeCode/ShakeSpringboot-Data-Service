package com.data.annotation;

import com.data.dbConfig.DataSourceEnum;
import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,ElementType.TYPE
})
public @interface DS {

    public DataSourceEnum value() default DataSourceEnum.master;
}
