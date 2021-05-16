package com.workday.java;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class NaiveJobQueue implements JobQueue {

    private Deque<Job> currentQueue;

    public NaiveJobQueue(List<Job> initialQueue) {
        currentQueue = new LinkedList<>();
        currentQueue.addAll(initialQueue);
    }

    @Override
    public synchronized Job pop() {
        if (currentQueue.isEmpty()) {
            try {
                Thread.sleep(Long.MAX_VALUE);
                throw new RuntimeException("end of the world");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            return currentQueue.pop();
        }
    }

    @Override
    public int length() {
        return currentQueue.size();
    }

    @Override
    public Deque<Job> currentQueue() {
        return this.currentQueue;
    }
}
