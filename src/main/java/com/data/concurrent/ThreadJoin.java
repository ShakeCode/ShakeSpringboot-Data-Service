package com.data.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadJoin {


    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
//        ThreadJoinTest();
//        CountDownLatchTest();
//        CyclicBarrierTest();
//        CylicBarrierTest2();    //优先执行情况
//        SemaphoreTest();
//        ExchangerTest();

    }

    private static void ExchangerTest() {
    /*  Exchanger（交换者）是一个用于线程间协作的工具类。Exchanger用于进行线程间的数据交
      换。它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。这两个线程通过
      exchange方法交换数据，如果第一个线程先执行exchange()方法，它会一直等待第二个线程也
      执行exchange方法，当两个线程都到达同步点时，这两个线程就可以交换数据，将本线程生产
      出来的数据传递给对方。*/
        final Exchanger<String> exgr = new Exchanger<String>();    //线程协助工具类
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String A = "银行流水A"; // A录入银行流水数据
                    exgr.exchange(A);
                } catch (InterruptedException e) {
                }
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String B = "银行流水B"; // B录入银行流水数据
                    String A = exgr.exchange(B);
                    System.out.println("交换数据:"+A);
                    System.out.println("A和B数据是否一致：" + A.equals(B) + "，A录入的是："
                            + A + "，B录入是：" + B);
                } catch (InterruptedException e) {
                }
            }
        });
        threadPool.shutdown();
    }

    private static void SemaphoreTest() {
        final int THREAD_COUNT = 10;
        final Semaphore s = new Semaphore(3); //信号量，进行流量控制，参数表示许可证数量，即最大并发数
        ExecutorService threadPool = Executors
                .newFixedThreadPool(THREAD_COUNT);

        for ( int i = 0; i< THREAD_COUNT; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        s.acquire();   //获得一个许可证
                        System.out.println("save data");
                        s.release();    //归还许可证
                    } catch (InterruptedException e) {
                    }
                }
            });
        }
        threadPool.shutdown();   //拒绝继续接受线程，等待线程执行完毕后关闭线程池
    }

    private static void CylicBarrierTest2() throws InterruptedException, BrokenBarrierException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("线程到达障碍点，优先执行此处，正在记录得分。。。");
            }
        });
        ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder().build();
        threadFactoryBuilder.newThread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("选手一开始跑。。。。");
        }).start();
        cyclicBarrier.await();
        System.out.println("选手二开始跑。。。。");
        System.out.println("选手到达终点，开始颁奖");
    }

    private static void CyclicBarrierTest() throws InterruptedException, BrokenBarrierException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3); //循环屏障，作用是让一组线程到达屏障点时被阻塞，直到最后一个线程到达，屏障关闭，被拦截的线程继续执行，参数标识屏障拦截的线程数
        ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder().build();
        threadFactoryBuilder.newThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("选手1开始。。。");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        threadFactoryBuilder.newThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("选手2开始。。。");
                try {
                    cyclicBarrier.await();   //告诉cyclicBarrier，我已经到达屏障点
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        cyclicBarrier.await();     //主程序到达障碍点，3个线程都到达障碍点，关闭障碍，线程继续执行，当线程数小于屏障拦截线程数时，线程永远阻塞，因为没有线程全部到达障碍，
        System.out.println("cyclicBarrier（），选手到达终点，开始颁奖。。。。");
    }

    private static void CountDownLatchTest() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder().build();
        threadFactoryBuilder.newThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("选手1开始。。。");
                countDownLatch.countDown();  //计数器减1
            }
        }).start();
        threadFactoryBuilder.newThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("选手2开始。。。");
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await(); //堵塞当前线程，等待所有线程执行完，计数器清0.主线程继续
        System.out.println("CountDownLatchTest（），选手抵达终点，开始颁奖。。。");
    }

    private static void ThreadJoinTest() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("选手1开始。。。");
            }
        }, "选手1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("选手2开始。。。");
            }
        }, "选手2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("ThreadJoinTest（），选手都到达终点");
    }
}
