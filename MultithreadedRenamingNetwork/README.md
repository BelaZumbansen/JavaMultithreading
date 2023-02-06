# MultithreadingRenamingNetwork
The implementation of a simple Bouncer can be assembled into a renaming network which allows you to renumber
threads ids to within a bounded range for a given number of threads.\
Develop a simulation program that constructs exactly one such renaming network, but which can be used
repeatedly to rename (renumber) threads.\
Create p ≥ 1 threads, your renaming network, and an integer array A. Each thread should have an integer
id value, initialized to -1. The array A should be of a fixed size, and as small as you can make it while
still holding any id value that may be assigned by your renaming network.\
Then perform n ≥ 1 rounds of renaming, each round giving each thread a new id according to the
renaming network. As soon as a thread knows its new id in a round, it (atomically) increments the count
at A[id], sleeps for a random time from 0 to 10ms, and then tries to re-enter the network for the next
round.\
You may only construct one splitter network, and exactly p threads. In this question you may only use
volatile data and the java.util.concurrent.atomic.* classes, as well as basic Thread
methods, such as start, join, sleep, currentThread, yield, and ThreadLocal data
if convenient. Do not use other forms of synchronization provided by Java.\
Your design should guarantee that every round every thread receives a unique id value. Verify this after
every thread has completed n renaming rounds, by (sequentially) checking that every value in the A array
is no more than n and the entire array sums to pn.
