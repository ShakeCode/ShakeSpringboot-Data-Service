package com.data.completeFeature;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompleteFeatureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        useTest();  //定义异步编程，阻塞，返回结果
//        runSyncTest();
//        supplyAsyncTest();

        //allOf
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello111");          //默认使用ForkJoinPool
            return "success111";
        });
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello222");          //默认使用ForkJoinPool
            return "success222";
        });
//        future.join();       //阻塞当前线程，后下个线程继续
        CompletableFuture.allOf(future, future1).join(); //异步连接，所有线程执行完毕，主线程继续
//        CompletableFuture.allOf(future, future1).get(); //异步连接，所有线程执行完毕，主线程继续
        //以上三种效果等价，都是等待所有线程执行完后线程继续，和并发工具类计时器countDownLatch 效果一致
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            future.completeExceptionally(new Exception()); //抛出异常
        }
        try {
            System.out.println(future1.get());
        } catch (InterruptedException  | ExecutionException e){
            future.completeExceptionally(new Exception()); //抛出异常
        }
        System.out.println("CompletableFuture");
    }

    private static void useTest() throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            // 模拟执行耗时任务
            System.out.println("task doing...");
            try {
                Thread.sleep(3000);
                int i = 1 / 0;
            } catch (InterruptedException e) {
                // 告诉completableFuture任务发生异常了
                completableFuture.completeExceptionally(e);
                e.printStackTrace();
            }
            // 告诉completableFuture任务已经完成,定义返回ok
            completableFuture.complete("ok");
        }).start();
        // 获取任务结果，如果没有完成会一直阻塞等待
        String result = completableFuture.get();
        System.out.println("计算结果:" + result);
    }

    private static void supplyAsyncTest() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello");          //默认使用ForkJoinPool
            return "success";
        });
        try {
//            future.complete("World"); //使用complete方法（只能调用一次，后续的重复调用会失效），直接返回，不等待get()阻塞完成
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            future.completeExceptionally(new Exception()); //抛出异常
        }
        System.out.println("CompletableFuture");
    }

    private static void runSyncTest() {
        //异步无返回值
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Hello");
        });
        try {
            future.get();         //阻塞当前线程，直到异步执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("CompletableFuture");
    }
}
