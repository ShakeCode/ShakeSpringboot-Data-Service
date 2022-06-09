package com.data.concurrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 执行定时任务线程后停止任务
 */
public class ScheduledThreadStop {

    public static void main(String[] args) throws InterruptedException {
        //周期执行任务
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(4);
        AtomicInteger atomicInteger = new AtomicInteger();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 执行第" + atomicInteger.incrementAndGet() + "次");
            countDownLatch.countDown(); //计数器减1
        }, 4, 4, TimeUnit.SECONDS);
        countDownLatch.await();              //阻塞线程往下执行，等待计数器减少为0
        scheduledExecutorService.shutdown(); //等待线程执行完毕后关闭线程池

    }
}
