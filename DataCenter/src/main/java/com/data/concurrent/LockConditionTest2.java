package com.data.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest2 {

    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition(); //同一把条件锁

    private static final List<String> list = new ArrayList<>();

    public static void product(){
        try {
            lock.lock();
            while(list.size() == 10){
                 condition.await();    //缓冲区满的时候停止生产
            }
            list.add("a");
            System.out.println("生产一个产品...缓冲区剩余："+list.size());
            condition.signal();   //通知消费者消费

            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static  void consume(){
        try {
            lock.lock();
            while(list.size() == 0){
                condition.await();    //缓冲区空的时候停止消费
            }
            list.remove(0);
            System.out.println("消费一个产品...缓冲区剩余："+list.size());
            condition.signal();    //通知生产者生产
            
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    static class Producter extends Thread {

        @Override
        public void run() {
            while (true) {
                product();
            }
        }
    }

    static class Consumer extends Thread {

        @Override
        public void run() {
            while (true) {
                consume();
            }
        }
    }

    public static void main(String[] args) {
        new Producter().start();
        new Consumer().start();
    }

}
