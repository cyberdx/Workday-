package com.workday.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * If creating a solution in Java, your code goes here
 */

public class JobRunnerImpl implements JobRunner {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile boolean shouldContinue = true;
    private volatile boolean shutdownFinished = false;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(100);

    @Override
    public void run(JobQueue jobQueue) {

        Set<Long> uniqueClient = jobQueue.currentQueue().parallelStream().map(Job::customerId).collect(Collectors.toSet());

        if (uniqueClient.size() > 10) {
            multiTasks(jobQueue, uniqueClient.size() * 10);
        }else {
            while (shouldContinue) {
                Job nextJob = jobQueue.pop();
                nextJob.execute();
            }
            logger.info("shutting down");
            shutdownFinished = true;
        }

    }

    private void multiTasks(JobQueue jobQueue, Integer poolSize) {
        ScheduledExecutorService priorityExecutor = Executors.newScheduledThreadPool(poolSize);
        Set<Long> uniqueClients = new HashSet<>();

        Map<Long, JobQueue> clients = new HashMap<>();

        jobQueue.currentQueue().forEach(j -> {
            if (!clients.containsKey(j.customerId())) {
                clients.put(j.customerId(), new JobQueueImp(new LinkedList<>()));
            }
            clients.get(j.customerId()).currentQueue().add(j);
        });

        clients.entrySet().parallelStream().map(v -> v.getValue().pop()).forEach(nextJob -> {
            if (uniqueClients.contains(nextJob.customerId())) {
                priorityExecutor.schedule(() -> {
                    try {
                        nextJob.execute();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                }, 1, TimeUnit.MILLISECONDS);
            } else {
                uniqueClients.add(nextJob.customerId());
                executor.schedule(() -> {
                    try {
                        nextJob.execute();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                }, 1, TimeUnit.MILLISECONDS);
            }
        });

        executor.shutdown();
        priorityExecutor.shutdown();
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
