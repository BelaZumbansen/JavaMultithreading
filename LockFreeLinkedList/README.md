# LockFreeLinkedList
Problem Description:\
This question disallows lock-based synchronization—you may not use synchronized, nor any of the 15
hardware atomics (TS, FA, CAS). You are allowed only to use basic R/W atomicity, and threads should
never spin/retry or be blocked (other than as described below). Note that this means you should not be
attempting to build your own locks either! You must still avoid data races of course.\
In this task your threads will simulate moving through while also dynamically changing a data structure.
To initialize the system create a circular linked-list (singly linked) consisting of 20 Nodes—build/define
your own list using next pointers, don’t use the built-in collections. Nodes will need unique names
for display, so give them numbers and define an appropriate toString method. Ensure the head of the
linked-list is globally available.\
Once the linked list is fully initialized start 3 threads: a traverser, a remover, and an adder.\
• traverser This thread traverses the linked list. Upon entry to a Node it prints out the Node name
(id) and then sleeps for 50ms before continuing to the next Node.\
• remover This thread also traverses the linked list (without printing); it will also need to keep track
of a Node, which we can call prior. Every time it enters the head of the linked list (including
when first starting) it sets prior to null. Like the traverser, it pauses on entry to a node, but for
a shorter time, just 10ms. The, if the current Node is not the head, it does one of the following:\
– 40% of the time If prior is null then it sets prior to the current Node.\
– 10% of the time If prior is not null then it removes all Nodes strictly between prior
and the current one from the linked-list. After doing so it should print out a message “cutting
from A to B“, replacing A and B with the prior and current Node name.\
  – 50% of the time It does nothing.\
• adder This thread also traverses the linked list without printing. However, its job is to add new
nodes to the linked list. Like the remover thread, it pauses 10ms on entry to a node. It then does
one of the following:\
  – k% of the time It splices in a new sublist of 3 new Nodes into the list between the current
Node and its next. After doing so it should print out a message “adding nodes after A“,
replacing A with the name of the current Node.\
  – 100-k% of the time It does nothing.\
Define a program that takes one input parameter (0.0 < k < 1), and choose a value of k that ensures
both the adder and remover do significant work. Start the threads and let the simulation run for 5s of
execution. Once 5s is reached, wait for each thread to complete its full traversal (reach the head again).
Once all 3 threads have surely stopped, print a newline and the list of nodes in the list (starting from the
head) to the console.\
Note that the activities of these threads necessarily conflict, and Nodes inserted may be deleted or vice
versa. You should nevertheless guarantee that threads do not crash, and that it is always possible for all
threads to continue traversing the list and reach the list head.\
Ensure concurrency is maximized—it should be possible for all threads to be performing their actions at
the same time.
