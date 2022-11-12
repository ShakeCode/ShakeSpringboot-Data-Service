package com.data.retry;

public class TestRetry {

    public static void main(String[] args) throws InterruptedException {
        Object obj = new RetryTemplate() {
            @Override
            public Object doBusiness() {
                double num = Math.random();
                int i = 1 / 0;  // 产生异常
                return num;
            }
        };
        Object result = ((RetryTemplate) obj).setRetry(10).setSleep(2000).excute();
        System.out.println(" get result is " + result);
    }
}
