package com.data.lambda;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.map.HashedMap;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lambda1 {

    public static void main(String[] args) throws Exception {
        List<Apple> list = new ArrayList<>();

        System.out.println(Optional.ofNullable(list).orElseGet(() -> {
            System.out.println("list为空!!");
            return new ArrayList<>();
        }).stream().filter(Objects::nonNull).map(apple -> apple.getMoney()).collect(Collectors.toSet()));
//                .map(apple->apple.get(0)).map(Apple::getName).orElse("李俊"));

        Map<String, Object> map1 = new HashMap();
        map1.put("name", "lijun");
        map1.put("age", 12);
        // 允许map为null,当map等于null 或者取name键值对应没有值，则返回默认值none,  取到则输出对应值
        System.out.println(Optional.ofNullable(map1).map(m -> m.get("name")).orElse("none"));
//        System.out.println(Optional.ofNullable(map1).map(m -> m.get("name1")).orElseThrow(() -> new Exception("没有该值")));
        System.out.println("返回map:" + Optional.ofNullable(map1).map(m -> m.get("name1")).orElseGet(() -> {
            return new HashedMap<String,Object>(){{put("name","kevin");}};
        }));

        System.out.println("是否不为空：" + Optional.ofNullable(map1).isPresent());
        Optional.ofNullable(map1).ifPresent(m -> {
            System.out.println("不为空,name=" + m.get("name"));
        });

        // lambdaTT();

        List<Apple> appleList = new ArrayList<>();//存放apple对象集合

        Apple apple1 = new Apple(1, "苹果1", new BigDecimal("3.25"), 10);
        Apple apple12 = new Apple(1, "苹果2", new BigDecimal("1.35"), 20);
        Apple apple2 = new Apple(2, "香蕉", new BigDecimal("2.89"), 30);
        Apple apple3 = new Apple(3, "荔枝", new BigDecimal("9.99"), 40);

        appleList.add(apple1);
        appleList.add(apple12);
        appleList.add(apple2);
        appleList.add(apple3);

        //List 以ID分组 Map<Integer,List<Apple>>
        Map<Integer, List<Apple>> groupBy = appleList.stream().collect(Collectors.groupingBy(Apple::getId));

        System.err.println("groupBy:" + groupBy);

        Map<Integer, Apple> appleMap = appleList.stream().collect(Collectors.toMap(Apple::getId, a -> a, (k1, k2) -> k1));

        System.out.println(appleMap);

        //过滤出符合条件的数据
        List<Apple> filterList = appleList.stream().filter(a -> a.getName().equals("香蕉")).collect(Collectors.toList());

        System.err.println("filterList:" + filterList);

        //计算 总金额
        BigDecimal totalMoney = appleList.stream().map(Apple::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.err.println("totalMoney:" + totalMoney);

        // 查找流中最大 最小值
        Optional<Apple> maxApple = appleList.stream().
                collect(Collectors.maxBy(Comparator.comparing(Apple::getNum)));
        maxApple.ifPresent(System.out::println);

        Optional<Apple> minApple = appleList.stream().
                collect(Collectors.minBy(Comparator.comparing(Apple::getNum)));
        minApple.ifPresent(System.out::println);


        // 根据id去重
        List<Apple> unique = appleList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Apple::getId))), ArrayList::new));
        System.out.println("ID apple:" + unique);
    }

    private static void lambdaTT() {
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
        Map<String, Object> nameMap = (Map) mapList.stream().collect(Collectors.toMap((k) -> {
            return (String) k.get("name");
        }, (v) -> {
            return v.get("age");
        }, (k1, k2) -> {
            return k2;
        }));
        System.out.println(nameMap);
        Map<String, Object> nameMap1 = (Map) mapList.stream().collect(Collectors.toMap((k) -> {
            return (String) k.get("name");
        }, Function.identity(), (k1, k2) -> {
            return k2;
        }));
        System.out.println(nameMap1);
        Map<String, Object> nameMap2 = (Map) mapList.stream().collect(HashMap::new, (newMap, oldMap) -> {
            newMap.put((String) oldMap.get("name"), oldMap.get("age"));
        }, HashMap::putAll);
        System.out.println(nameMap2);
        Set<Integer> ageSet = (Set) mapList.stream().filter((m) -> {
            return !m.get("name").equals("xiaoming");
        }).map((m) -> {
            return (Integer) m.get("age");
        }).collect(Collectors.toSet());
        System.out.println(ageSet);
        Stream var10000 = mapList.stream();
        PrintStream var10001 = System.out;
        System.out.getClass();
        var10000.forEach(var10001::println);
        Stream<String> stringStream = Stream.of("1", "2");
        List<String> stringList = (List) stringStream.collect(Collectors.toList());
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
        List var14 = (List) Stream.of("11", "22", "33").flatMap((m) -> {
            return Arrays.stream(new String[]{m});
        }).collect(Collectors.toList());
        var10001 = System.out;
        System.out.getClass();
        var14.forEach(var10001::println);
    }
}
