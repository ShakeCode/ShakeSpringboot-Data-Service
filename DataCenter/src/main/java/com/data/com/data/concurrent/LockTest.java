package com.data.com.data.concurrent;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {
    private Lock lock = new ReentrantLock();  //显示锁

    private Object data = null;//共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据。
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    /*  java的对象锁和类锁：java的对象锁和类锁在锁的概念上基本上和内置锁是一致的，但是，两个锁实际是有很大的区别的，对象锁是用于对象实例方法，或者一个对象实例上的，类锁是用于类的静态方法或者一个类的class对象上的。我们知道，类的对象实例可以有很多个！！！，但是每个类只有一个class对象，所以不同对象实例的对象锁是互不干扰的，但是每个类只有一个类锁！！！！。但是有一点必须注意的是，其实类锁只是一个概念上的东西，并不是真实存在的，它只是用来帮助我们理解锁定实例方法和静态方法的区别的。*/

    public static void main(String[] args) {
        LockTest lockT = new LockTest();
/*        new Thread(() -> {
            //静态同步方法，同步方法同时执行/，对象锁与类锁作用不同域，所以会出现交替进行
//            lockT.syncA();
//            lockT.testLock();
//            lockT.syncB();
//            lockT.staticSync();
            lockT.syncA();
        }, "test1").start();

        new Thread(() -> {
//            lockT.staticSync();
//            lockT.testLock();
//            lockT.syncBlock();
//            lockT.syncObjectLock();
            lockT.syncBlock();
        }, "test2").start();*/
        new Thread() {
            public void run() {
                lockT.readLockTest();
            }
        }.start();

        new Thread() {
            public void run() {
                lockT.writeLockTest(new Random().nextInt(10000));
            }
        }.start();
    }

    public synchronized void syncA() {
        //内置锁，对象锁（方法锁），排斥独占锁，悲观锁
        //执行时获取对象锁，其他线程阻塞，执行完后释放锁，发生异常时jvm中断线程释放锁
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= 0.5) {
            System.out.println(Thread.currentThread() + "正在执行");
        }
        System.out.println(Thread.currentThread() + "执行完毕");
    }

    public synchronized void syncB() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= 0.5) {
            System.out.println(Thread.currentThread() + "正在执行");
        }
        System.out.println(Thread.currentThread() + "执行完毕");
    }

    public static synchronized void staticSync() {
        //类锁，一个对象只有一个类锁，两个线程相互执行静态同步方法和同步方法，会出现线程交替情况
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= 0.5) {
            System.out.println(Thread.currentThread() + "正在执行");
        }
        System.out.println(Thread.currentThread() + "执行完毕");
    }

    public void testLock() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread() + "获取锁");
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime <= 0.5) {
                System.out.println(Thread.currentThread() + "正在执行");
            }
            System.out.println(Thread.currentThread() + "执行完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread() + "释放锁");
        }
    }

    public void syncBlock() {
        //对象锁,和同步方法一致，在一起执行时，一个线程获得锁，另一个线程阻塞
        synchronized (this) {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime <= 0.5) {
                System.out.println(Thread.currentThread() + "正在执行");
            }
        }
        System.out.println(Thread.currentThread() + "执行完毕");
    }


    public void syncObjectLock() {
        //类锁，和静态同步方法一致，在一起执行时，一个线程获得锁，另一个线程阻塞
        synchronized (LockTest.class) {
            //对象锁
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime <= 0.5) {
                System.out.println(Thread.currentThread() + "正在执行");
            }
        }
        System.out.println(Thread.currentThread() + "执行完毕");
    }

    public void readLockTest() {
        //线程进入写锁的前提条件：
        //    没有其他线程的读锁
        //    没有其他线程的写锁
        rwl.readLock().lock();//上读锁，其他线程只能读不能写
        System.out.println(Thread.currentThread().getName() + " be ready to read data!");
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwl.readLock().unlock(); //释放读锁，最好放在finnaly里面
        }
        System.out.println(Thread.currentThread().getName() + "have read data :" + data);
    }

    public void writeLockTest(Object data) {
        rwl.writeLock().lock();//上写锁，不允许其他线程读也不允许写
        System.out.println(Thread.currentThread().getName() + " be ready to write data!");
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwl.writeLock().unlock();//释放写锁
        }
        this.data = data;
        System.out.println(Thread.currentThread().getName() + " have write data: " + data);

    }


}
