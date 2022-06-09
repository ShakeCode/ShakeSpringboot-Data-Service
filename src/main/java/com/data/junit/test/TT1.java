package com.data.junit.test;

import java.util.Scanner;

public class TT1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuffer sbu = new StringBuffer();
        String ss = scanner.nextLine();
        String[] arr = ss.split(" ");
        for (int i = arr.length - 1; i >= 0; i--) {
            System.out.print(arr[i]);
            System.out.print(" ");
        }
    }
}
