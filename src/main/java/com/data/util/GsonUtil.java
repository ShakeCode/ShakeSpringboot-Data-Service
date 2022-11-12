package com.data.util;

import com.data.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.json.JsonSanitizer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Gson util.
 * @since 2020 -09-05
 */
public class GsonUtil {
    private static Gson GSON;

    private GsonUtil() {
    }

    private static Gson getGson() {
        if (GSON == null) {
            // 双重校验锁 缓存
            synchronized (GsonUtil.class) {
                if (GSON == null) {
                    GSON = (new GsonBuilder()).enableComplexMapKeySerialization().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
                }
            }
        }
        return GSON;
    }


    /**
     * Json 2 java bean t.
     * @param <T>      the type parameter
     * @param json     the json
     * @param classOfT the class of t
     * @return the t
     */
    public static <T> T json2JavaBean(String json, Class<T> classOfT) {
        return getGson().fromJson(JsonSanitizer.sanitize(json), classOfT);
    }

    /**
     * Map 2 bean t.
     * @param <T>   the type parameter
     * @param map   the map
     * @param clazz the clazz
     * @return the t
     */
    public static <T> T map2Bean(Map<String, T> map, Class<T> clazz) {
        String toJson = getGson().toJson(map);
        return getGson().fromJson(toJson, clazz);
    }

    /**
     * Json 2 bean list list. 对象泛型
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the list
     */
    public static <T> List<T> json2BeanList(String json, Class<T> clazz) {
        List<T> mList = new ArrayList();
        JsonArray array = (new JsonParser()).parse(JsonSanitizer.sanitize(json)).getAsJsonArray();
        Iterator var4 = array.iterator();

        while (var4.hasNext()) {
            JsonElement elem = (JsonElement) var4.next();
            mList.add(getGson().fromJson(elem, clazz));
        }

        return mList;
    }

    /**
     * Json 2 java bean list list. 转对象list
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the list
     */
    public static <T> List<T> json2JavaBeanList(String json, Class<T> clazz) {
        List<T> result = new ArrayList();
        List<Map<String, T>> mList = (List) getGson().fromJson(JsonSanitizer.sanitize(json), List.class);
        Iterator var4 = mList.iterator();

        while (var4.hasNext()) {
            Map<String, T> t = (Map) var4.next();
            result.add(map2Bean(t, clazz));
        }

        return result;
    }

    /**
     * Json 2 list t. 转list泛型
     * @param <T>  the type parameter
     * @param json the json
     * @return the t
     */
    public static <T> T json2List(String json) {
        return getGson().fromJson(JsonSanitizer.sanitize(json), (new TypeToken<List<T>>() {
        }).getType());
    }

    /**
     * Json 2 map map.
     * @param <T>        the type parameter
     * @param json the json string
     * @return the map
     */
    public static <T> Map<String, T> json2Map(String json) {
        return (Map) getGson().fromJson(JsonSanitizer.sanitize(json), (new TypeToken<Map<String, T>>() {
        }).getType());
    }

    /**
     * From json t.
     * @param <T>     the type parameter
     * @param json    the json
     * @param typeOfT the type of t
     * @return the t
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(JsonSanitizer.sanitize(json), typeOfT);
    }

    /**
     * To json string.
     * @param obj the obj
     * @return the string
     */
    public static String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String ss = "{\"name\":\"123\",\"age\":12}";
        System.out.println("json 转对象:" + json2JavaBean(ss, Person.class));
        Person person = (Person) fromJson(ss, (new TypeToken<Person>() {
        }).getType());
        System.out.println("json 转对象:" + person);
        Person person1 = (Person) fromJson(ss, Person.class);
        System.out.println("json 转对象:" + person1);
        String str1 = "[{\"name\":\"123\",\"age\":12}]";
        List<Map<String, Object>> list = (List) json2List(str1);
        System.out.println("json 转map-list:" + list);
        String str2 = "[\"a\",\"b\",\"c\"]";
        List<String> list2 = (List) json2List(str2);
        System.out.println("json 转对象list-string:" + list2);
        String str3 = "[[{\"name\":\"123\",\"age\":12}]]";
        List<List<Map<String, Object>>> list3 = (List) json2List(str3);
        System.out.println("json 转对象list-list-map:" + list3);
        System.out.println("对象转json:" + toJson(person));
        System.out.println("json 转map:" + json2Map(ss));
        System.out.println("map 转bean:" + map2Bean(json2Map(ss), Person.class));
        System.out.println("json 转对象list:" + json2BeanList(str1, Person.class));
        System.out.println("json 转对象list:" + json2JavaBeanList(str1, Person.class));
    }
}
