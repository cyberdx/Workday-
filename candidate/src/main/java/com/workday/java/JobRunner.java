package com.workday.java;

/**
 * ***********************
 * Do not modify this file
 * ***********************
 */

/**
 * This is interface that you will need to implement. A JobRunner encapsulates
 * the logic to schedule the execution of jobs.
 */
public interface JobRunner {

    /**
     * Continuously consumes the job queue and executes the jobs,
     * execution continues forever until shutdown is invoked.
     */
    void run(JobQueue jobQueue);

    /**
     * Stops consuming jobs from the queue and blocks until
     * all jobs that have been dequeued have finished their execution.
     */
    void shutdown();
}
