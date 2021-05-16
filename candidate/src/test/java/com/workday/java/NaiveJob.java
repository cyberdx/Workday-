package com.workday.java;

import java.util.Random;

public class NaiveJob implements Job {

    private Random random = new Random();

    private long customerId;
    private long uniqueId = random.nextLong();
    private int duration;
    private boolean executed = false;

    public NaiveJob() {
        customerId = random.nextLong();
        duration = 100;
    }

    public NaiveJob(long customerId, int duration) {
        this.customerId = customerId;
        this.duration = duration;
    }
    @Override
    public long customerId() {
        return customerId;
    }

    @Override
    public long uniqueId() {
        return uniqueId;
    }

    @Override
    public int duration() {
        return duration;
    }

    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(duration);
            executed = true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
