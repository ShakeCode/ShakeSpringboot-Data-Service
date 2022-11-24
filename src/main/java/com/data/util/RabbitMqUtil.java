/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Rabbit mq util.
 */
public class RabbitMqUtil {
    private static final ObjectMapper OM = new ObjectMapper();

    private RabbitMqUtil() {

    }

    /*
     * 所有对 mq 消息 序列化要求都在这里单独配置
     */
    static {
        // 时间解析器
        OM.registerModule(new JavaTimeModule());
    }

    /**
     * mq 消息序列化
     * @param o the o
     * @return the string
     * @author lingting 2020-11-24 17:30
     */
    @SneakyThrows
    public static String serialization(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        return OM.writeValueAsString(o);
    }

    /**
     * mq消息反序列化
     * @param <T> the type parameter
     * @param str the str
     * @param t   the t
     * @return the t
     * @author lingting 2020-11-24 17:30
     */
    @SneakyThrows
    public static <T> T deserialization(String str, Class<T> t) {
        if (t.equals(String.class)) {
            return (T) str;
        }
        return OM.readValue(str, t);
    }

    /**
     * Gets om.
     * @return the om
     */
    public static ObjectMapper getOM() {
        return OM;
    }

    /**
     * To list bean list.json字符串转为List
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the list
     */
    public static <T> List<T> toListBean(String json, Class<T> clazz) {
        JavaType type = getCollectionType(ArrayList.class, clazz);
        try {
            return OM.readValue(json, type);
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }

    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OM.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 将json字符串转为 class中泛型的实体
     * @param <T>    the type parameter
     * @param str    json字符串
     * @param aClass 类
     * @return t t
     */
    public static <T> T toGenericsBean(String str, Class<?> aClass) {
        T value;
        Type type = ((ParameterizedType) aClass.getGenericSuperclass()).getActualTypeArguments()[0];
        if (type.getTypeName().equals(String.class.getName())) {
            return (T) str;
        }
        if (type instanceof Class) {
            value = deserialization(str, (Class<T>) type);

        } else {
            value = deserialization(str, (Class<T>) Object.class);
        }
        if (value == null) {
            return null;
        }
        if (value instanceof Collection) {
            Class<?> actualTypeArguments = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
            value = (T) toListBean(str, actualTypeArguments);
        }

        return value;
    }

}