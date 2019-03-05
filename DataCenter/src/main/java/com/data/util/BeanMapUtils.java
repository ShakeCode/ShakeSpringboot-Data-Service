package com.data.util;

import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class BeanMapUtils {

    public static void main(String[] args) {
        Person person = new BeanMapUtils().new Person();
        person.setAge(12);
        person.setName("李军");               
        Map<String, Object> map =  beanToMap(person);
        System.out.println(map);
    }

    public static Map<String, Object> beanToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        if (object == null) {
            return map;
        }
        BeanMap beanMap = BeanMap.create(object);
        beanMap.forEach((k, v) -> {
            map.put(k.toString(), v);
        });
        return map;
    }

    class Person {
        private String name;
        private Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
