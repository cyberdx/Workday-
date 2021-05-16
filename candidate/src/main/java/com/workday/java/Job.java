package com.workday.java;

/**
 * ***********************
 * Do not modify this file
 * ***********************
 */

/**
 * Defines a piece of work to be executed on behalf of a customer
 */
public interface Job {
    // Uniquely identifies the customer associated with this job
    long customerId();

    // Uniquely identifies the job. 2 jobs with the same
    // uniqueId should behave identically when executed
    long uniqueId();

    // Estimated time in milliseconds that the job will take to execute
    int duration();

    // Execute the job
    void execute();
}
