package com.workday.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NaiveJobRunner implements JobRunner {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile boolean shouldContinue = true;
    private volatile boolean shutdownFinished = false;

    @Override
    public void run(JobQueue jobQueue) {
        while (shouldContinue) {
            Job nextJob = jobQueue.pop();
            nextJob.execute();
        }
        logger.info("shutting down");
        shutdownFinished = true;
    }

    @Override
    public void shutdown() {
        shouldContinue = false;
        while (!shutdownFinished) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
