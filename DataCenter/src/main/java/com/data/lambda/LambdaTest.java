package com.data.lambda;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LambdaTest {

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("线程test");
        }).start();

        //listMap -> list
        List<Map<String, Object>> list = new ArrayList();
        for (int i = 1; i < 3; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", i);
            map.put("age", i);
            list.add(map);
        }

        if (!CollectionUtils.isEmpty(list)) {
            //listmap转set
            Set<String> setName = list.stream().filter(mm -> ((Integer)mm.get("age") > 1) ).map(m -> m.get("name").toString()).collect(Collectors.toSet());
            //复杂过滤器
            List<String> setFilterList = list.stream().filter(k -> {
                if(k.containsKey("name")){
                      return true;
                }
                return false;
            }).map(m -> m.get("name").toString()).collect(Collectors.toList());

            //listmap转list
            List<String> setList = list.stream().map(m -> m.get("name").toString()).collect(Collectors.toList());
            //分组
            Map<String, List<Map<String, Object>>> mapGroup = list.stream().collect(Collectors.groupingBy(m -> m.get("name").toString()));

            //listmap - > map
            Map<String, Object> map1  = list.stream().collect(Collectors.toMap(k -> k.get("name").toString(),v->(Integer)v.get("age"),(key1,key2)->key2));
            System.out.println(setName);
            System.out.println(setFilterList);
            System.out.println(setList);
            System.out.println(mapGroup);
            System.out.println(map1);
        }
    }
}
