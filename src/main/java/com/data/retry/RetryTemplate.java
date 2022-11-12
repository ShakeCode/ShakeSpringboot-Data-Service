package com.data.retry;

public abstract class RetryTemplate {

    private static final int DEFAULT_SLEEP_TIME = 2000;

    private static final int DEFAULT_RETRY_TIME = 3;

    private int sleep;

    private int retry;

    // 可被重写的业务方法
    public abstract Object doBusiness();

    public int getSleep() {
        return sleep;
    }

    public RetryTemplate setSleep(int sleep) {
        this.sleep = sleep;
        return this;
    }

    public int getRetry() {
        return retry;
    }

    public RetryTemplate setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    public Object excute() throws InterruptedException {
        for (int i = 0, time = this.retry; i < time; i++) {
            System.out.println(" retry " + (i + 1) + " time ...");
            try {
                // 书写业务方法
                return doBusiness();
            } catch (Exception e) {
                Thread.sleep(sleep);
            }
        }
        return null;
    }
}
