package com.data.concurrent.DelayQueueTest;

import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;

public class DelayedQueueTask {

    private static DelayQueue<OrderItem> delayQueue = new DelayQueue<>();

    public static void main(String[] args) {
        delayQueue.add(new OrderItem(2000, 12));
        delayQueue.add(new OrderItem(3000, 20));
        delayQueue.add(new OrderItem(5000, 30));
        System.out.println(LocalDateTime.now());
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(LocalDateTime.now() + ",获取数据:" + delayQueue.take().getData());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
