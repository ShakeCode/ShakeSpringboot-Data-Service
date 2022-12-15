/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util.builder;

import com.data.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Builder.
 * @param <T> the type parameter
 * @author Administrator
 */
public class Builder<T> {
    private final Supplier1<T> supplier;

    private List<Consumer1<T>> list = new ArrayList<>();

    /**
     * Instantiates a new Builder.
     * @param supplier the supplier
     */
    public Builder(Supplier1<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Of builder.
     * @param <T>      the type parameter
     * @param supplier the supplier
     * @return the builder
     */
    public static <T> Builder<T> of(Supplier1<T> supplier) {
        return new Builder<T>(supplier);
    }

    /**
     * Width builder.
     * @param <V>      the type parameter
     * @param consumer the consumer
     * @param value    the value
     * @return the builder
     */
    public <V> Builder<T> width(Consumer2<T, V> consumer, V value) {
        list.add(e -> consumer.accept(e, value));
        return this;
    }

    /**
     * Width builder.
     * @param <V>      the type parameter
     * @param consumer the consumer
     * @param value    the value
     * @param orElse   the or else
     * @return the builder
     */
    public <V> Builder<T> width(Consumer2<T, V> consumer, V value, V orElse) {
        if (null == value) {
            list.add(e -> consumer.accept(e, value));
        } else {
            list.add(e -> consumer.accept(e, orElse));
        }
        return this;
    }

    /**
     * Width builder.
     * @param <V>      the type parameter
     * @param accept   the accept
     * @param consumer the consumer
     * @param value    the value
     * @return the builder
     */
    public <V> Builder<T> width(boolean accept, Consumer2<T, V> consumer, V value) {
        if (accept) {
            list.add(e -> consumer.accept(e, value));
        }
        return this;
    }

    /**
     * Width builder.
     * @param <V>      the type parameter
     * @param accept   the accept
     * @param consumer the consumer
     * @param value    the value
     * @param orElse   the or else
     * @return the builder
     */
    public <V> Builder<T> width(boolean accept, Consumer2<T, V> consumer, V value, V orElse) {
        if (accept) {
            list.add(e -> consumer.accept(e, value));
        } else {
            list.add(e -> consumer.accept(e, orElse));
        }
        return this;
    }

    /**
     * Build t.
     * @return the t
     */
    public T build() {
        T t = supplier.get();
        Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .forEach(e -> e.accept(t));
        return t;
    }

    /**
     * The interface Consumer 2.
     * @param <T> the type parameter
     * @param <V> the type parameter
     */
    @FunctionalInterface
    public interface Consumer2<T, V> {
        /**
         * accept 2 values
         * @param t     the t
         * @param value the value
         */
        void accept(T t, V value);
    }

    /**
     * The interface Consumer 1.
     * @param <T> the type parameter
     */
    @FunctionalInterface
    public interface Consumer1<T> {
        /**
         * accept 1 value
         * @param t the t
         */
        void accept(T t);
    }

    /**
     * The interface Supplier 1.
     * @param <T> the type parameter
     */
    @FunctionalInterface
    public interface Supplier1<T> {

        /**
         * Gets a result.
         * @return a result
         */
        T get();
    }


    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Person student = Builder.of(Person::new)
                .width(Person::setName, "张三1")
                .width(Person::setName, "张三2", "张三3")
                .width(1 == 2, Person::setName, "张三4")
                .width(1 == 2, Person::setName, "张三5", "张三6")
                .width(Person::setAge, 11)
                .build();
        System.out.println(student.toString());
    }
}
