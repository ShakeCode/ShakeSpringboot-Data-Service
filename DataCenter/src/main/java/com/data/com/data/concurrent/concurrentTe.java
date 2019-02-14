package com.data.com.data.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

public class concurrentTe {

    static volatile int count;

    static ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

    static {
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setKeepAliveSeconds(10);
        threadPoolTaskExecutor.setMaxPoolSize(21);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.initialize();
    }

    ThreadLocal<StringBuffer> threadLocal = new ThreadLocal<StringBuffer>() {

        @Override
        public StringBuffer initialValue() {

            return new StringBuffer();
        }
    };

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        countDownLatchTe();

        futureTest();
        System.out.println("线程计算结束 主程序继续。。。。。");
    }

    private static void countDownLatchTe() {
        final CountDownLatch countDownLatch = new CountDownLatch(4);
        for (int i = 0, size = 4; i < size; i++) {
            threadPoolTaskExecutor.execute(() -> {
                try {
                    System.out.println("------ local thread :" + Thread.currentThread().getName());
                    count++;
                    Thread.sleep(2);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await(1, TimeUnit.MINUTES);
            System.out.println("count:" + count);
            System.out.println("countDownLatch await success");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPoolTaskExecutor.shutdown();
    }

    public static void futureTest() throws ExecutionException, InterruptedException {

        StringBuffer stringBuffer = new StringBuffer();

        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("子线程1在进行计算");
                int sum = 0;
                for (int i = 0; i < 10000000; i++)
                    sum += i;
                stringBuffer.append("子线程1计算结果:" + sum);
                return sum + "";
            }
        });
        FutureTask<String> futureTask1 = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("子线程2在进行计算");
                int sum = 0;
                for (int i = 0; i < 50000000; i++)
                    sum += i;
                stringBuffer.append(",子线程2计算结果:" + sum);
                return sum + "";
            }
        });
        System.out.println("执行 threadPoolTaskExecutor start....");
        threadPoolTaskExecutor.submit(futureTask);
        threadPoolTaskExecutor.submit(futureTask1);
//        CompletedFuture<String>  completedFuture = new CompletedFuture<String>();
        threadPoolTaskExecutor.shutdown();
        try {
            System.out.println("task 线程1是否完成：" + futureTask.isDone());
            System.out.println("task 线程1运行结果" + futureTask.get());
            System.out.println("task 线程2运行结果" + futureTask1.get());
            System.out.println("task stringBuffer 结果:" + stringBuffer.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("所有任务执行完毕");
    }


}
