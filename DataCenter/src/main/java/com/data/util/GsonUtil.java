package com.data.util;

import com.data.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson GSON;

    private GsonUtil() {
    }

    private static Gson getGson() {
        if (GSON == null) {
            Class var0 = GsonUtil.class;
            synchronized(GsonUtil.class) {
                if (GSON == null) {
                    GSON = (new GsonBuilder()).enableComplexMapKeySerialization().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
                }
            }
        }

        return GSON;
    }

    public static <T> T json2JavaBean(String json, Class<T> classOfT) {
        return getGson().fromJson(json, classOfT);
    }

    public static <T> T map2Bean(Map<String, T> map, Class<T> clazz) {
        String toJson = getGson().toJson(map);
        return getGson().fromJson(toJson, clazz);
    }

    public static <T> List<T> json2BeanList(String json, Class<T> clazz) {
        List<T> mList = new ArrayList();
        JsonArray array = (new JsonParser()).parse(json).getAsJsonArray();
        Iterator var4 = array.iterator();

        while(var4.hasNext()) {
            JsonElement elem = (JsonElement)var4.next();
            mList.add(getGson().fromJson(elem, clazz));
        }

        return mList;
    }

    public static <T> List<T> json2JavaBeanList(String json, Class<T> clazz) {
        List<T> result = new ArrayList();
        List<Map<String, T>> mList = (List)getGson().fromJson(json, List.class);
        Iterator var4 = mList.iterator();

        while(var4.hasNext()) {
            Map<String, T> t = (Map)var4.next();
            result.add(map2Bean(t, clazz));
        }

        return result;
    }

    public static <T> T json2List(String json) {
        return getGson().fromJson(json, (new TypeToken<List<T>>() {
        }).getType());
    }

    public static <T> Map<String, T> json2Map(String gsonString) {
        return (Map)getGson().fromJson(gsonString, (new TypeToken<Map<String, T>>() {
        }).getType());
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }

    public static String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    public static void main(String[] args) {
        String ss = "{\"name\":\"123\",\"age\":12}";
        System.out.println("json 转对象:" + json2JavaBean(ss, Person.class));
        Person person = (Person)fromJson(ss, (new TypeToken<Person>() {
        }).getType());
        System.out.println("json 转对象:" + person);
        Person person1 = (Person)fromJson(ss, Person.class);
        System.out.println("json 转对象:" + person1);
        String str1 = "[{\"name\":\"123\",\"age\":12}]";
        List<Map<String, Object>> list = (List)json2List(str1);
        System.out.println("json 转map-list:" + list);
        String str2 = "[\"a\",\"b\",\"c\"]";
        List<String> list2 = (List)json2List(str2);
        System.out.println("json 转对象list-string:" + list2);
        String str3 = "[[{\"name\":\"123\",\"age\":12}]]";
        List<List<Map<String, Object>>> list3 = (List)json2List(str3);
        System.out.println("json 转对象list-list-map:" + list3);
        System.out.println("对象转json:" + toJson(person));
        System.out.println("json 转map:" + json2Map(ss));
        System.out.println("map 转bean:" + map2Bean(json2Map(ss), Person.class));
        System.out.println("json 转对象list:" + json2BeanList(str1, Person.class));
        System.out.println("json 转对象list:" + json2JavaBeanList(str1, Person.class));
    }
}
