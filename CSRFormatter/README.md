# CSRFormatter
Project Description:\
This question requires you use OpenMP in a C program. “Sparse matrices” are matrices characterized 15
by having a high proportion of 0 values in the array. Storing these as a regular, 2D arrays can be quite
wasteful, and thus special formats exist that compress the storage, storing only the non-0 values. One
popular format is Compressed Sparse Row or CSR. In this format three 1D arrays are used to store the
actual values, their column indices, and a compressed vector describes the rows.

The values array lists the actual (non-0) array element values. The cols array gives the column index
of each non-0 value. The rowptr gives the offset to the cols (and values) array of the first non-0
entry in that row. Thus the difference between rowptr[i] and rowptr[i+1] tells us the number
of non-0 values in row i. Note that the cols and values arrays are the same length as the number of
non-0 entries, while the rowptr array has the same length as the number of array rows (in the example
above one extra entry (7) is shown at the end of the rowptr, but we could infer that if we otherwise
know the total number of non-0s).

The task in this question is to convert a regular array into CSR format, using OpenMP to parallelize that
operation. Your (C) program should be invoked as:\
csr n p s\
The n > 3 parameter is the array dimension (assume a square, n × n matrix). The array should be
filled with random 0 or 1 values, with 0.0 ≤ p ≤ 1.0 used as the probability of a given value being a 0.
This means that if p == 1.0 then the array should be all 0s, and if p == 0.0 the array should be all 1s.
The s parameter is a random seed—initialize the random number generator to this value in order to get
reproducible results.

Once the array is constructed, print it out to the console in a readable array form (one row per line, column
values space-separated). Serially determine the number of non-0s and allocate the 3 1D arrays required.
Then convert it to CSR format, and print out the rowptr, cols, and values arrays, each on one line.

Your approach should use OpenMP to parallelize the initial array construction as well as the conversion.
Disable the I/O and time the array creation and conversion phases, using a reasonably large n, for p ∈
{0.05, 0.2, 0.5} for both the sequential execution and an OpenMP execution (using the same seed values
for comparability).
