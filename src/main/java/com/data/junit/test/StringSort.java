//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.data.junit.test;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class StringSort {
    public StringSort() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        TreeSet<String> hashSet = new TreeSet();

        for(int i = 0; i < num; ++i) {
            String ss = scanner.next();
            StringBuffer sbu = new StringBuffer(ss);
            sbu.reverse();
            System.out.println(sbu.toString());
            hashSet.add(ss);
        }

        Iterator ite = hashSet.iterator();

        while(ite.hasNext()) {
            System.out.println((String)ite.next());
        }

    }
}
