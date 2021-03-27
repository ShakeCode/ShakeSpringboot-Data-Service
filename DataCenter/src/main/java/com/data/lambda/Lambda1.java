package com.data.lambda;

import com.google.common.collect.Lists;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lambda1 {

    public static void main(String[] args) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Map<String, Object> map1 = new HashMap();
        map1.put("name", "lijun");
        map1.put("age", 12);
        Map<String, Object> map2 = new HashMap();
        map2.put("name", "xiaoming");
        map2.put("age", 20);
        Map<String, Object> map3 = new HashMap();
        map3.put("name", "chenxiang");
        map3.put("age", 30);
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);
        System.out.println(mapList);
        Map<String, Object> nameMap = (Map)mapList.stream().collect(Collectors.toMap((k) -> {
            return (String)k.get("name");
        }, (v) -> {
            return v.get("age");
        }, (k1, k2) -> {
            return k2;
        }));
        System.out.println(nameMap);
        Map<String, Object> nameMap1 = (Map)mapList.stream().collect(Collectors.toMap((k) -> {
            return (String)k.get("name");
        }, Function.identity(), (k1, k2) -> {
            return k2;
        }));
        System.out.println(nameMap1);
        Map<String, Object> nameMap2 = (Map)mapList.stream().collect(HashMap::new, (newMap, oldMap) -> {
            newMap.put((String)oldMap.get("name"), oldMap.get("age"));
        }, HashMap::putAll);
        System.out.println(nameMap2);
        Set<Integer> ageSet = (Set)mapList.stream().filter((m) -> {
            return !m.get("name").equals("xiaoming");
        }).map((m) -> {
            return (Integer)m.get("age");
        }).collect(Collectors.toSet());
        System.out.println(ageSet);
        Stream var10000 = mapList.stream();
        PrintStream var10001 = System.out;
        System.out.getClass();
        var10000.forEach(var10001::println);
        Stream<String> stringStream = Stream.of("1", "2");
        List<String> stringList = (List)stringStream.collect(Collectors.toList());
        System.out.println(stringList);
        Stream<Double> generateA = Stream.generate(new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        });
        Stream<Double> generateB = Stream.generate(() -> {
            return Math.random();
        });
        Stream<Double> generateC = Stream.generate(Math::random);
        var10000 = Stream.of("apple", "orange", "pear").limit(2L);
        var10001 = System.out;
        System.out.getClass();
        var10000.peek(var10001::println).collect(Collectors.toList());
        var10000 = Stream.iterate(2, (x) -> {
            return x + 2;
        }).limit(3L);
        var10001 = System.out;
        System.out.getClass();
        var10000.forEach(var10001::println);
        List var14 = (List)Stream.of("11", "22", "33").flatMap((m) -> {
            return Arrays.stream(new String[]{m});
        }).collect(Collectors.toList());
        var10001 = System.out;
        System.out.getClass();
        var14.forEach(var10001::println);
    }
}
