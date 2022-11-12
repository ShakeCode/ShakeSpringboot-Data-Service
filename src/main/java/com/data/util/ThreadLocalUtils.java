package com.data.util;

public class ThreadLocalUtils {

    //父子线程共享本地线程变量
    private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<String>();

    //设置线程需要保存的值
    public static void setValue(String str) {
        THREAD_LOCAL.set(str);
    }

    //获取线程中保存的值
    public static String getValue() {
        return THREAD_LOCAL.get();
    }

    //移除线程中保存的值
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
