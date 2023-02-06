# MultithreadedAssemblySim
Problem Description:\
An assembly-line simulation involves a final product assembly, 5 intermediate assembly stations, and 3
reservoirs of basic parts. Each intermediate assembly station builds some component of the final product
using one or more basic parts, and potentially some assembled components from other stations. The final
product assembly combines all partial assemblies into the final product.

First, build the basic simulation. Use separate threads to represent each assembly station, reservoir, and
the final product assembler. The final assembly station requires parts from some intermediate stations,
while intermediate stations require parts from the reservoir(s) and/or other intermediate stations. This
stations dependencies are as per the dependency graph shown below (R is for reservoir, F is the final
assembly).\

Each station and reservoir has a part-generation speed, taking (randomly) between 0 and p milliseconds
to produce a new part. Parts produced are modelled as unique objects, and stored in a set, represented by
a linked list associated with the reservoir.

Each station gathers its necessary components and converts them into a new part. For intermediate stations the parts produced are also stored as a unique, individual object added to a linked list associated with the station, while for the final station it just needs to keep a count of parts produced. Each intermediate
part-list should have a maximum capacity, c; if the capacity is reached then a new part cannot be created
by that station until one of its parts is consumed. Once k final products are produced the simulation
should end.

Note that in this design each station/reservoir part-list will experience threads attempting to add to the
list (the station/reservoir producing the part), and some number of threads attempting to remove items
from the list (other stations needing a part produced by that station/reservoir). You must use your own
implementations of a linked list for the part storage (do not use any built-in Java ones), but how threads
add/remove from the list is up to you, as is whether your list is singly- or doubly-linked, circular, etc.

(a) Build the simulation as described above and use your own implementation of a blocking linked-list
to handle the simulation as described above. Provide a program invoked as:\
java simulation p c k\
Choose values of 1 < p < 30, and k > 1000 such that the simulation (usually) takes measureable
time to execute with c = 1. Try higher values of c (at least 4 different ones) and observe the time it
takes for the simulation to complete. In a separate document give data on the program performance
you observed, and explain it in relation to your machine characteristics and the parameter values
chosen.

(b) Now, change the internal linked-list to a lock-free implementation, as described in the lectures and
text and include a separate implementation, simulation2.java.
