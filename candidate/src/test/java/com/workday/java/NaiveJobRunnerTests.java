package com.workday.java;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * These are integration tests that cover the basic requirements of a JobRunner,
 * the NaiveJobRunner does not meet all of them so some of these tests are ignored.
 * Your implementation of JobRunner should pass this tests, feel free to copy
 * this class and adapt it to your solution.
 */
public class NaiveJobRunnerTests {

    @Test
    public void shouldEventuallyExecuteAllJobs() throws InterruptedException {
        List<Job> jobs = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            jobs.add(new NaiveJob());
        }
        // There are 5 jobs of 100ms each = 500ms of cpu time
        JobQueue testQueue = new NaiveJobQueue(jobs);
        JobRunner jobRunner = new NaiveJobRunner();
        new Thread(() -> jobRunner.run(testQueue)).start();
        Thread.sleep(1000); // 1s is enough to execute all jobs even for the naive implementation
        assertEquals(testQueue.length(), 0);
        for (Job job : jobs) {
            NaiveJob naiveJob = (NaiveJob) job;
            assertTrue(naiveJob.isExecuted());
        }
    }

    @Test
    @Ignore
    // Fails in the naive implementation
    public void shouldExecuteJobsWithPerformance() throws InterruptedException {
        List<Job> jobs = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            jobs.add(new NaiveJob());
        }
        // There are 20 jobs of 100ms each = 2s of cpu time
        JobQueue testQueue = new NaiveJobQueue(jobs);
        JobRunner jobRunner = new NaiveJobRunner();
        new Thread(() -> jobRunner.run(testQueue)).start();
        Thread.sleep(1000); // Only 1s wait should be enough for all jobs to be executed
        for (Job job : jobs) {
            NaiveJob naiveJob = (NaiveJob) job;
            assertTrue(naiveJob.isExecuted());
        }
    }

    @Test
    @Ignore
    // Fails in the naive implementation
    public void shouldExecuteJobsWithFairness() throws InterruptedException {
        List<Integer> customerIds = new ArrayList<>();
        for(int i = 1; i <= 100; i++) {
            customerIds.add(i);
        }
        List<Job> jobs = new ArrayList<>();
        for(Integer customerId: customerIds) {
            for(int i = 1; i <= 1000; i++) {
                jobs.add(new NaiveJob(customerId, 100));
            }
        }
        // There are 100000 jobs of 100ms each
        JobQueue testQueue = new NaiveJobQueue(jobs);
        JobRunner jobRunner = new NaiveJobRunner();
        new Thread(() -> jobRunner.run(testQueue)).start();
        Thread.sleep(10000); // This should be enough to execute about 10% of the jobs on a modern pc
        for(Integer customerId : customerIds) {
            int executedJobs = 0;
            for(Job job: jobs) {
                if(((NaiveJob) job).isExecuted() && job.customerId() == customerId.intValue()) {
                    executedJobs++;
                }
            }
            // For every customer there should be at least 1 executed job
            assertTrue(executedJobs > 0);
        }
    }

    @Test
    public void shouldShutdownGracefully() throws InterruptedException {
        List<Job> jobs = Arrays.asList(new NaiveJob(), new NaiveJob(), new NaiveJob(), new NaiveJob());
        JobQueue testQueue = new NaiveJobQueue(jobs);
        JobRunner jobRunner = new NaiveJobRunner();
        Thread runningThread = new Thread(() -> jobRunner.run(testQueue));
        runningThread.start();
        jobRunner.shutdown();
        assertTrue(testQueue.length() > 0);
    }

}
