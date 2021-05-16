Please document here:
* Classes you have implemented or modified, including test classes
JobImp - no changes, similar to origin
JobQueueImp - no changes, similar to origin
JobRunnerImpl - new method multiTasks(JobQueue && poolSize)
JobRunnerImplTest - similar to origin except names

* Any assumptions that affected your design
Design I kept as it is.

* Any shortcomings of your implementation
The number of threads is dynamic value

* An explanation of your definition of fairness execution
If there is a group of clients, we divide them into subgroups, and we launch each group in our own thread pool, thus all clients have the same priority.
