package com.data.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件控制锁，实现生产者，消费者模式
 *
 * 生产者不断生产，放进产品进入缓存区，当缓存区满的时候就停止生产
 * 消费者不断消费，向缓冲区取出产品消费，当缓存区没产品时候就不能继续消费产品
 * 以此达到不浪费生产者的时间，生产者继续生产，消费者继续消费，也不会停滞等待
 */
public class LockConditionTest {

    static class Apple {
        private String color;
        private Integer size;

        public Apple(String color, Integer size) {
            this.color = color;
            this.size = size;
        }
    }

    static class Busket {
        private Lock lock = new ReentrantLock();  //锁住当前篮子
        private Condition product = lock.newCondition();        //生产者线程锁条件
        private Condition consume = lock.newCondition();        //消费者线程锁条件

        private List<Apple> apples = new ArrayList<>(10);

        private void productApple() {
            lock.lock();
            try {
                while (apples.size() == 10) {         //缓冲区，只能存储10个苹果
                    System.out.println("停止生产苹果...");
                    product.await();  //挂起当前生产者线程
                }
                System.out.println("生产一个苹果...");
                apples.add(new Apple("red", 10));
                System.out.println("正在通知消费者吃苹果...");
                consume.signal(); //通知消费者线程消费篮子里的苹果 , 唤醒consume.await()的线程
                System.out.println("=====================================");

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        private void consumeApple() {
            lock.lock();
            try {
                while (apples.size() == 0) {
                    System.out.println("停止消费苹果...");
                    consume.await();  //挂起当前消费者线程
                }
                apples.remove(0);
                System.out.println("消费一个苹果...");
                System.out.println("正在通知生产者生产苹果...");
                product.signal(); //通知生产者线程生产苹果放进篮子

                System.out.println("=====================================");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    static class Consumer extends Thread {
        private Busket busket;

        public Consumer(Busket busket) {
            this.busket = busket;
        }

        @Override
        public void run() {
            while (true) {
                busket.consumeApple();
            }
        }
    }

    static class Producter extends Thread {
        private Busket busket;

        public Producter(Busket busket) {
            this.busket = busket;
        }

        @Override
        public void run() {
            while (true) {
                busket.productApple();
            }
        }
    }

    public static void main(String[] args) {
        Busket busket = new Busket();
        new Producter(busket).start();
        new Consumer(busket).start();
    }

}
