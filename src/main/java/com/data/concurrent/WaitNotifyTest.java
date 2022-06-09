package com.data.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.LockSupport;

/**
 * Wait & Notify 实现生产消费者模式
 */
public class WaitNotifyTest {

    static class Waimai {
        private Queue<Integer> queue;
        private Integer maxSize;

        public Waimai(Queue<Integer> queue, Integer maxSize) {
            this.queue = queue;
            this.maxSize = maxSize;
        }
    }

    static class Product extends Thread {
        private Waimai waimai;

        public Product(Waimai waimai) {
            this.waimai = waimai;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (waimai) {
                    while (waimai.queue.size() < waimai.maxSize) {
                        waimai.queue.add(1);
                        System.out.println(Thread.currentThread().getName() + "生产一个外卖...");
                        try {
                            waimai.wait();    //保证在同步块中运行,挂起自己线程，释放对象锁，等待被唤醒
                            Thread.sleep(2000);   //休眠2秒，腾出cpu给其他线程，2秒后继续运行
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class Consumer extends Thread {
        private Waimai waimai;

        public Consumer(Waimai waimai) {
            this.waimai = waimai;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (waimai) {
                    while (waimai.queue.size() > 0) {
                        waimai.queue.remove();
                        System.out.println(Thread.currentThread().getName() + "吃掉一个外卖...");
                        waimai.notify();   //保证在同步块中运行,保证拿得锁后运行，唤醒其他线程竞争对象锁
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        /*Waimai waimai = new Waimai(new LinkedList<>(), 10);
        new Product(waimai).start();
        new Consumer(waimai).start();*/

        testLockSupport();
    }

    public static void testLockSupport() {
        final List<String> list = new ArrayList<>();
        Thread A = new Thread(() -> {
            while (true) {
                if (list.size() < 10) {
                    list.add("1");
                    System.out.println(Thread.currentThread().getName() + "生产一个外卖...");
                    LockSupport.park();
                    
                }
            }
        });
        A.start();

        Thread B = new Thread(() -> {
            while (true) {
                if (list.size() > 0) {
                    list.remove(0);
                    System.out.println(Thread.currentThread().getName() + "吃掉一个外卖....");
                    LockSupport.unpark(A);
                }
            }
        });
        B.start();

    }

}
