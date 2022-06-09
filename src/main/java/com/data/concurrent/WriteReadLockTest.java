package com.data.concurrent;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁互斥，都是可重入锁，读写锁能有效提高读比写多的场景下的程序性能
 * 读写锁在同一时刻可以允许多个读线程访问，但是在写线程访问时，所有的读线程和其他写线程均被阻塞
 */
public class WriteReadLockTest {

    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();  //读写锁
    private static final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();  //读锁
    private static final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();     //写锁

    private static final List<String> list = new ArrayList<>();
    private static final Queue<String> queue = new LinkedList<>();
    private static final Random random = new Random(10);

    public static void read() {
        try {
            readLock.lock();
            System.out.println("读取一个数据:" + queue.peek());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public static void write() {
        try {
            writeLock.lock();
            System.out.println("写入数据:" + queue.add(random.nextInt() + "a"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                write();
            }).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                read();
            }).start();
        }
    }

}
