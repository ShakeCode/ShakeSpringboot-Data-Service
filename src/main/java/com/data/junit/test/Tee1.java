package com.data.junit.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tee1 extends Thread {


    private static final String MESSAGE = "taobao";


   /* 考察点1：重载静态多分派——根据传入重载方法的参数类型，选择更加合适的一个重载方法
    考察点2：static方法不能被子类覆写，在子类中定义了和父类完全相同的static方法，则父类的static方法被隐藏，Son.staticmethod()或new Son().staticmethod()都是调用的子类的static方法，如果是Father.staticmethod()或者Father f = new Son(); f.staticmethod()调用的都是父类的static方法。
    考察点3：此题如果都不是static方法，则最终的结果是A. 调用子类的getType，输出collection*/

    public static void main(String[] args) {
       /* Java使用了中间缓存变量机制：
        i=i++;等同于：
        temp=i； (等号右边的i)
        i=i+1;      (等号右边的i)
        i=temp;   (等号左边的i)
        而i=++i;则等同于：
        i=i+1;
        temp=i;
        i=temp;*/
        int i = 0;
        i = i++;
        System.out.println("i:" + i); // 输出0

        Object obj = 0.2;
        System.out.println(obj.getClass());     // 默认double

        Collection<?>[] collections =
                {new HashSet<String>(), new ArrayList<String>(), new HashMap<String, String>().values()};
        Super subToSuper = new Sub();
        for (Collection<?> collection : collections) {
            System.out.println(subToSuper.getType(collection));
        }
    }

    abstract static class Super {

        // 可以没有抽象方法，去除不报错
        public abstract String getType1(Collection<?> collection);

        public static String getType(Collection<?> collection) {  // 静态方法不被子类重写
            return "Super: collection";
        }

        public static String getType(List<?> list) {
            return "Super: list";
        }

        public String getType(ArrayList<?> list) {
            return "Super: arrayList";
        }

        public static String getType(Set<?> set) {
            return "Super: set";
        }

        public String getType(HashSet<?> set) {
            return "Super: hashSet";
        }
    }

    static class Sub extends Super {
        @Override
        public String getType1(Collection<?> collection) {
            return "Sub1";
        }

        public static String getType(Collection<?> collection) {
            return "Sub";
        }
    }
}
