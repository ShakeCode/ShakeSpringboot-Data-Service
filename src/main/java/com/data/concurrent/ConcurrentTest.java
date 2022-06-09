package com.data.concurrent;

public class ConcurrentTest {

    private static Integer num = 0;

    static class  A extends Thread{

        @Override
        public void run() {
            for(int i = 0; i<5;i++){
                   num++;
            }
        }
    }

    static class  B extends Thread{

        @Override
        public void run() {
            for(int i = 0; i<5;i++){
                num--;
            }
        }
    }

    public static void main(String[] args) {
        new A().start();
        new B().start();
        System.out.println(num);
    }
}
