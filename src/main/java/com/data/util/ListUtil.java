/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;

import com.data.model.Person;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type List util.
 */
public class ListUtil {
    /**
     * Distinct by key predicate.
     * @param <T>          the type parameter
     * @param keyExtractor the key extractor
     * @return the predicate
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>(1);
        return t -> Objects.isNull(seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE));
    }

    /**
     * Map list.
     * @param <T>      the type parameter
     * @param <K>      the type parameter
     * @param list     the list
     * @param function the function
     * @return the list
     */
    public static <T, K> List<K> map(Collection<T> list, Function<T, K> function) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(function).collect(Collectors.toList());
    }

    /**
     * Map distinct list.
     * @param <T>      the type parameter
     * @param <K>      the type parameter
     * @param list     the list
     * @param function the function
     * @return the list
     */
    public static <T, K> List<K> mapDistinct(Collection<T> list, Function<T, K> function) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().filter(distinctByKey(function)).map(function).collect(Collectors.toList());
    }

    /**
     * Filter and map list.
     * @param <T>         the type parameter
     * @param <K>         the type parameter
     * @param list        the list
     * @param biPredicate the bi predicate
     * @param function    the function
     * @param distinct    the distinct
     * @return the list
     */
    public static <T, K> List<K> filterAndMap(
            Collection<T> list, Predicate<T> biPredicate, Function<T, K> function, boolean distinct) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        if (!distinct) {
            return list.stream().filter(biPredicate).map(function).collect(Collectors.toList());
        }
        return list.stream().filter(biPredicate).map(function).distinct().collect(Collectors.toList());
    }

    /**
     * Filter list.
     * @param <T>         the type parameter
     * @param list        the list
     * @param biPredicate the bi predicate
     * @return the list
     */
    public static <T> List<T> filter(Collection<T> list, Predicate<T> biPredicate) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().filter(biPredicate).collect(Collectors.toList());
    }

    /**
     * List to map map.
     * @param <T>           the type parameter
     * @param <K>           the type parameter
     * @param list          the list
     * @param keyMapper     the key mapper
     * @param valueMapper   the value mapper
     * @param mergeFunction the merge function
     * @return the map
     */
    public static <T, K> Map<?, ?> listToMap(Collection<T> list, Function<T, K> keyMapper, Function<T, K> valueMapper, BinaryOperator<K> mergeFunction) {
        if (isEmpty(list)) {
            return new HashMap<>(1);
        }
        // Map<String, String> nmap = list.stream().collect(HashMap::new,(k, v) -> k.put(v.getName(), v.getAge()), HashMap::putAll);
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    /**
     * List to map map.
     * @param <T>         the type parameter
     * @param <K>         the type parameter
     * @param list        the list
     * @param keyMapper   the key mapper
     * @param valueMapper the value mapper
     * @return the map
     */
    public static <T, K> Map<?, ?> listToMap(Collection<T> list, Function<T, K> keyMapper, Function<T, K> valueMapper) {
        if (isEmpty(list)) {
            return new HashMap<>(1);
        }
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, (k1, k2) -> k2));
    }

    /**
     * Group by map.
     * @param <T>      the type parameter
     * @param <K>      the type parameter
     * @param list     the list
     * @param function the function
     * @return the map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> list, Function<T, K> function) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.groupingBy(function));
    }

    /**
     * For each.
     * @param <T>    the type parameter
     * @param list   the list
     * @param action the action
     */
    public static <T> void forEach(List<T> list, Consumer<? super T> action) {
        if (isEmpty(list)) {
            return;
        }
        list.stream().forEach(action);
    }

    /**
     * Is empty boolean.
     * @param list the list
     * @return the boolean
     */
    public static boolean isEmpty(Collection list) {
        return list == null || list.size() == 0;
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        List<Person> list = Lists.newArrayList(
                Person.builder().name("dream").age(10).build(),
                Person.builder().name("dream").age(12).build(),
                Person.builder().name("jack").age(12).build()
        );
        List<Person> distinctPersonList = list.stream().filter(distinctByKey(Person::getName).and(distinctByKey(Person::getAge))).collect(Collectors.toList());
        System.out.println(distinctPersonList);
        System.out.println(ListUtil.map(list, Person::getAge));
        System.out.println(ListUtil.mapDistinct(list, Person::getAge));
        System.out.println(ListUtil.filterAndMap(list, data -> data.getAge() > 9, Person::getAge, true));
        System.out.println(ListUtil.filter(list, data -> data.getAge() > 11));
        System.out.println("k-v:" + ListUtil.listToMap(list, data -> data.getName(), data -> data.getAge(), (old1, new2) -> new2));
        System.out.println("k-v:" + ListUtil.listToMap(list, data -> data.getName(), data -> data.getAge()));
        System.out.println("分组:" + ListUtil.groupBy(list, data -> data.getAge()));

        ListUtil.forEach(list, System.out::println);
        // distinctPerson(list);
    }

    private static void distinctPerson(List<Person> list) {
        // 单字段去重
        List<Person> collect = list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(p -> p.getAge()))), ArrayList::new));
        System.out.println(collect);

        // 多字段去重
        List<Person> disCollect = list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(p -> p.getName() + ";" + p.getAge()))), ArrayList::new));
        System.out.println(disCollect);
    }
}
