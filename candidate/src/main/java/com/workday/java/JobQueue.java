package com.workday.java;

/**
 * ***********************
 * Do not modify this file
 * ***********************
 */

import java.util.Deque;

/**
 * As jobs are launched by customers they are placed on this queue by an
 * external system, this interface provides the job runner the methods required
 * to retrieve those jobs.
 * Important: do not treat this as a fixed predefined collection of job, this
 * is meant to abstract the interaction between the JobRunner and the external
 * system that holds the customers requests.
 */
public interface JobQueue {

    // Removes a job from the queue. If the queue has been drained,
    // this call will block until a new job becomes available
    Job pop();

    // Returns the current number of enqueued jobs, note that this
    // number might increase or decrease as new jobs are enqueued by
    // customer or are consumed by the JobRunner
    int length();

    Deque<Job> currentQueue();
}
