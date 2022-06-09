package com.data.concurrent.DelayQueueTest;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class OrderItem implements Delayed {

    private long expiredTime;

    private long delayedTime;

    private Object data;

    public OrderItem(long delayedTime, Object data) {
        this.delayedTime = delayedTime;
        this.data = data;
        this.expiredTime = System.currentTimeMillis() + delayedTime;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public long getDelayedTime() {
        return delayedTime;
    }

    public void setDelayedTime(long delayedTime) {
        this.delayedTime = delayedTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
