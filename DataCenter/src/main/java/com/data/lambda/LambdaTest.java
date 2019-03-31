package com.data.lambda;

import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class LambdaTest {

    public static void main(String[] args) {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "你好";
            }
        };
        System.out.println(supplier.get());
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("trrr");
//        testLambda();
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return Integer.valueOf(s);
            }
        }.andThen(m -> m + 5);
        System.out.println(function.apply("55"));
        //传入一个类型参数，返回一个类型参数
        Function<String, Integer> function1 = a -> Integer.valueOf(a);
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                if (StringUtils.isEmpty(s)) {
                    return true;
                }
                return false;
            }
        };
        System.out.println(predicate.test("fd"));
        Predicate<String> predicate1 = m -> m.equals("hello");
        //传入两个类型参数，返回一个类型结果
        BiFunction<String,String,Integer> biFunction = (a,b) -> Integer.valueOf(a+b);
        System.out.println(biFunction.apply("11","22"));
    }

    private static void testLambda() {
        new Thread(() -> {
            System.out.println("线程test");
        }).start();

        //listMap -> list
        List<Map<String, Object>> list = new ArrayList();
        for (int i = 1; i < 3; i++) {
            Map<String, Object> map = new HashMap<>();
            String str = MapUtils.getString(map, "hello");
            map.put("name", i);
            map.put("age", i);
            list.add(map);
        }

        if (!CollectionUtils.isEmpty(list)) {
            //listmap转set
            Set<String> setName = list.stream().filter(mm -> ((Integer) mm.get("age") > 1)).map(m -> m.get("name").toString()).collect(Collectors.toSet());
            //复杂过滤器
            List<String> setFilterList = list.stream().filter(k -> {
                if (k.containsKey("name")) {
                    return true;
                }
                return false;
            }).map(m -> m.get("name").toString()).collect(Collectors.toList());

            //listmap转list
            List<String> setList = list.stream().map(m -> m.get("name").toString()).collect(Collectors.toList());
            //分组
            Map<String, List<Map<String, Object>>> mapGroup = list.stream().collect(Collectors.groupingBy(m -> m.get("name").toString()));

            //listmap - > map
            Map<String, Object> map1 = list.stream().collect(Collectors.toMap(k -> k.get("name").toString(), v -> (Integer) v.get("age"), (key1, key2) -> key2));
            System.out.println(setName);
            System.out.println(setFilterList);
            System.out.println(setList);
            System.out.println(mapGroup);
            System.out.println(map1);
        }
    }
}
