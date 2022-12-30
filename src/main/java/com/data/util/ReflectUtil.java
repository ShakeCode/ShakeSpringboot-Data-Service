/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;


import com.data.model.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Reflect util.
 */
public class ReflectUtil {
    /**
     * 直接获取对象属性值
     * @param obj       对象实例
     * @param fieldName 属性名称
     * @return field value
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null) return null;
        Object value = null;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            if (fields[j].getName().equals(fieldName)) {
                fields[j].setAccessible(true);
                // 字段值  
                try {
                    value = fields[j].get(obj);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    /**
     * 通过get方法获取值
     * @param obj            实例对象
     * @param fieldName      属性名称
     * @param parameterTypes 函数的参数
     * @return field value by method
     */
    public static Object getFieldValueByMethod(Object obj, String fieldName, Class<?>... parameterTypes) {
        if (obj == null) return null;
        Object value = null;

        try {
            Method method = obj.getClass().getMethod(fieldGetMethodName(fieldName));
            value = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    private static String fieldGetMethodName(String fieldName) {
        String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return getMethod;
    }

    /**
     * 设置值，包括key中间"."的情况
     * @param object the object
     * @param key    the key
     * @param value  the value
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setValue(Object object, String key, Object value) {

        //用来处理多数据问题
        Object oldObj = object;

        Object obj = object;
        String attr = null;
        while (key.indexOf(".") != -1) {
            attr = key.substring(0, key.indexOf("."));
            key = key.substring(key.indexOf(".") + 1);
            if (obj instanceof Map) {
                obj = ((Map) obj).get(attr);
            }
            if (obj instanceof List) {
                for (Object o : (List<?>) oldObj) {
                    if (o instanceof Map) {
                        obj = ((Map) o).get(attr);
                    } else {
                        obj = getFieldValue(o, attr);
                    }
                }
            } else {
                obj = getFieldValue(obj, attr);
            }
        }

        if (obj != null) {

            if (obj instanceof Map) {
                if (oldObj instanceof List) {
                    for (Object o : (List<?>) oldObj) {
                        if (obj instanceof Map) {
                            if (attr != null) {
                                o = getFieldValue(o, attr);
                                ((Map) o).put(key, value);
                            }
                        } else {
                            setFieldValue(o, key, value);
                        }
                    }
                } else {
                    ((Map) obj).put(key, value);
                }

            } else {
                if (oldObj instanceof List) {
                    for (Object o : (List<?>) oldObj) {
                        if (obj instanceof Map) {
                            if (attr != null) {
                                o = getFieldValue(o, attr);
                                ((Map) o).put(key, value);
                            }
                        } else {
                            setFieldValue(o, key, value);
                        }
                    }
                } else {
                    setFieldValue(obj, key, value);
                }
            }
        }
    }

    /**
     * 设置值
     * @param obj       the obj
     * @param fieldName the field name
     * @param val       the val
     * @return field value
     */
    public static Object setFieldValue(Object obj, String fieldName, Object val) {
        if (obj == null) return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            if (fields[j].getName().equals(fieldName)) {
                fields[j].setAccessible(true);
                // 字段值  
                try {
                    String type = fields[j].getType().getSimpleName();
                    //System.out.println("type : " + type);
                    if (val instanceof String) {
                        String value = val.toString();
                        if ("int".equals(type)) {
                            if (!StringUtils.isEmpty(value)) {
                                fields[j].setInt(obj, Integer.valueOf(value));
                            }
                        } else if ("long".equals(type)) {
                            if (!StringUtils.isEmpty(value)) {
                                fields[j].setLong(obj, Long.valueOf(value));
                            }
                        } else if ("double".equals(type)) {
                            if (!StringUtils.isEmpty(value)) {
                                fields[j].setDouble(obj, Double.valueOf(value));
                            }
                        } else if ("float".equals(type)) {
                            if (!StringUtils.isEmpty(value)) {
                                fields[j].setFloat(obj, Float.valueOf(value));
                            }
                        } else if ("Integer".equals(type)) {
                            if (StringUtils.isNotBlank(value)) {
                                fields[j].set(obj, Integer.valueOf(value));
                            } else {
                                fields[j].set(obj, null);
                            }
                        } else if ("Long".equals(type)) {
                            if (StringUtils.isNotBlank(value)) {
                                fields[j].set(obj, Long.valueOf(value));
                            } else {
                                fields[j].set(obj, null);
                            }
                        } else if ("Float".equals(type)) {
                            if (StringUtils.isNotBlank(value)) {
                                fields[j].set(obj, Float.valueOf(value));
                            } else {
                                fields[j].set(obj, null);
                            }
                        } else if ("Double".equals(type)) {
                            if (StringUtils.isNotBlank(value)) {
                                fields[j].set(obj, Double.valueOf(value));
                            } else {
                                fields[j].set(obj, null);
                            }
                        } else {
                            fields[j].set(obj, val);
                        }
                    } else {
                        fields[j].set(obj, val);
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return val;
    }

    /**
     * 是否存在该属性
     * @param obj       对象实例
     * @param fieldName 属性名
     * @return boolean boolean
     */
    public static boolean isExistField(Object obj, String fieldName) {
        boolean r = false;
        if (obj != null) {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                if (fields[j].getName().equals(fieldName)) {
                    r = true;
                    break;
                }
            }
        }
        return r;
    }


    /**
     * 单个对象的所有键值
     * @param obj 单个对象
     * @return Map<String, Object>   map 所有 String键 Object值
     */
    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val;
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            /*
             * String type = f.getType().toString();//得到此属性的类型 if
             * (type.endsWith("String")) {
             * System.out.println(f.getType()+"\t是String"); f.set(obj,"12") ;
             * //给属性设值 }else if(type.endsWith("int") ||
             * type.endsWith("Integer")){
             * System.out.println(f.getType()+"\t是int"); f.set(obj,12) ; //给属性设值
             * }else{ System.out.println(f.getType()+"\t"); }
             */

        }
        //        System.out.println("单个对象的所有键值==反射==" + map.toString());
        return map;
    }

    /**
     * 单个对象的某个键的值
     * @param obj 对象
     * @param key 键
     * @return Object 键在对象中所对应得值 没有查到时返回空字符串
     */
    public static Object getValueByKey(Object obj, String key) {
        // 得到类对象
        Class userCla = obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            try {

                if (f.getName().endsWith(key)) {
                    //                    System.out.println("单个对象的某个键的值==反射==" + f.get(obj));
                    return f.get(obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 没有查到时返回空字符串
        return "";
    }

    /**
     * 多个（列表）对象的所有键值
     * @param object the object
     * @return List<Map < String, Object>  > 列表中所有对象的所有键值
     */
    public static List<Map<String, Object>> getKeysAndValues(List<Object> object) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object obj : object) {
            Class userCla;
            // 得到类对象
            userCla = obj.getClass();
            /* 得到类中的所有属性集合 */
            Field[] fs = userCla.getDeclaredFields();
            Map<String, Object> listChild = new HashMap<>();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = new Object();
                try {
                    val = f.get(obj);
                    // 得到此属性的值
                    listChild.put(f.getName(), val);// 设置键值
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            list.add(listChild);// 将map加入到list集合中
        }
        //        System.out.println("多个（列表）对象的所有键值====" + list.toString());
        return list;
    }

    /**
     * 多个（列表）对象的某个键的值
     * @param object the object
     * @param key    the key
     * @return List<Object>   键在列表中对应的所有值
     */
    public static List<Object> getValuesByKey(List<Object> object, String key) {
        List<Object> list = new ArrayList<>();
        for (Object obj : object) {
            // 得到类对象
            Class userCla = obj.getClass();
            /* 得到类中的所有属性集合 */
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                try {
                    if (f.getName().endsWith(key)) {
                        list.add(f.get(obj));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //        System.out.println("多个（列表）对象的某个键的值列表====" + list.toString());
        return list;
    }

    /**
     * map转换为对象
     * @param clazz the clazz
     * @param map   the map
     * @return object object
     * @throws Exception the exception
     */
    public static Object map2JavaBean(Class<?> clazz, Map<String, Object> map) throws Exception {
        Object javabean = clazz.newInstance(); // 构建对象
        Method[] methods = clazz.getMethods(); // 获取所有方法
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String field = method.getName(); // 截取属性名
                field = field.substring(field.indexOf("set") + 3);
                field = field.toLowerCase().charAt(0) + field.substring(1);
                if (map.containsKey(field)) {
                    method.invoke(javabean, map.get(field));
                }
            }
        }
        return javabean;
    }

    /**
     * Copy t.
     * @param <T>    the type parameter
     * @param source the source
     * @return the t
     */
    public static <T> T copy(Object source) {
        Class<?> clz = source.getClass();
        //获取指定对象的Class对象
        /* 为什么要多加一个参数Class<T> clz?
         * 是因为泛型, 为了返回值直接是该对象, 而不是Object.
         */
        T newObj = null;
        try {
            //根据class对象创建当前类型的实例(空对象)
            newObj = (T) clz.newInstance();
            //获取当前类中包含的所有属性
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                //拼接获取setter/getter方法的名称
                String setMethodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                //根据方法名称获取方法对象
                Method method_set = clz.getMethod(setMethodName, field.getType());//setter方法
                Method method_get = clz.getMethod(getMethodName);//getter方法
                //执行源对象指定属性的getter方法，获取属性值
                Object returnval = method_get.invoke(source);
                //执行新对象的setter方法，将源对象的属性值设置给新对象
                method_set.invoke(newObj, returnval);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return newObj;
    }

    /**
     * Copy obj property.
     * @param source the source
     * @param target the target
     */
    public static void copyObjProperty(Object source, Object target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        Field[] fields = sourceClass.getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            String fieldName = field.getName();
            Object sourceValue = null;
            try {
                sourceValue = field.get(source);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
      /*      if (sourceValue instanceof String) {
                sourceValue = source;
            } else if (sourceValue instanceof Number) {
                // long, double, float, int ,byte...
                sourceValue = sourceValue.toString();
            } else if (sourceValue instanceof Boolean) {
                sourceValue = BooleanUtils.toStringTrueFalse((Boolean) sourceValue);
            } else {
                throw new ServiceException("unsupport this type to invoke");
            }*/
            Field targetField = ReflectionUtils.findField(targetClass, fieldName);
            if (Optional.ofNullable(targetField).isPresent() && Objects.nonNull(sourceValue)) {
                ReflectionUtils.makeAccessible(targetField);
                ReflectionUtils.setField(targetField, target, sourceValue);
            }
        }
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Person person = new Person("liming", 12);
        System.out.println(ReflectUtil.isExistField(person, "age"));
        System.out.println(ReflectUtil.getFieldValue(person, "age"));
        System.out.println(ReflectUtil.setFieldValue(person, "age", 20));
        System.out.println(ReflectUtil.getFieldValue(person, "age"));

        Person pp = ReflectUtil.copy(person);
        System.out.println(person.hashCode());
        System.out.println(pp.hashCode());
        System.out.println(pp.toString());
        System.out.println(pp.equals(person));

        Person person1 = new Person();
        ReflectUtil.copyObjProperty(person, person1);
        System.out.println("person1:" + person1);
    }
}
