#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>

int n;
float p;
int s;

int main(int argc, char **argv) {

    if (argc != 4) {
        printf("Please pass the required 3 parameters for execution of this program.");
        return 0;
    }

    if (sscanf (argv[1], "%i", &n) != 1) {
        printf("First parameter must be an integer value");
        return 0;
    }

    if (sscanf (argv[2], "%f", &p)!=1) {
        printf("Second parameter must be a float value");
        return 0;
    }

     if (sscanf (argv[3], "%i", &s) != 1) {
        printf("Third parameter must be an integer value");
        return 0;
    }

    /** Initialize random number generator with the seed */
    srand(s);

    struct timeval start1, end1, start2, end2;
    double elapsed1, elapsed2;
    int (*arr)[n] = malloc(sizeof(int[n][n]));
    int i, j, ndx;
    double (*random)[n] = malloc(sizeof(double[n][n]));

    // Generate a random number for each matrix entry
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
            random[i][j] = ((double) rand() / (RAND_MAX));
        }
    }

    // Start first timer
    gettimeofday(&start1, NULL);

    // Parallelize the outer loop of the for loop
    #pragma omp parallel for private(j)
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {

            if (random[i][j] <= p) {
                arr[i][j] = 0;
            } else {
                arr[i][j] = 1;
            }
        }
    }

    gettimeofday(&end1, NULL);
    elapsed1 = end1.tv_sec + end1.tv_usec / 1e6 - start1.tv_sec - start1.tv_usec / 1e6; 
    printf("Array Construction: %f\n", elapsed1);

    int cnt = 0;
    
    // Count the number of non-zero entries and print the array
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++)  {

           if (arr[i][j] == 1) {
               cnt++;
           }
           printf("%i ", arr[i][j]); 
        }
        printf("\n");
    }

    // Allocate space for the arrays we need
    int *values = (int*) malloc(cnt * sizeof(int));
    int *cols = (int*) malloc(cnt * sizeof(int));
    int *nonzeroper = (int*) malloc(n * sizeof(int));
    int *rowptr = (int*) malloc(n * sizeof(int));

    for (i = 0; i < n; i++) {
        rowptr[n] = -1;
    }

    for (i = 0; i < cnt; i++) {
        cols[i] = -1;
        values[i] = -1;
    }

    ndx = 0;

    gettimeofday(&start2, NULL);

    // Determine number of nonzero entries per row in parallel  
    #pragma omp parallel for private(j) shared (nonzeroper, arr)
    for (i = 0; i < n; i++) {

        int nonZeroCnt = 0;
        for(j = 0; j < n; j++) {
            
            if(arr[i][j] != 0) {
                nonZeroCnt++;
            }
        }
        nonzeroper[i] = nonZeroCnt;
    }

    // Determine the row pointer array
    rowptr[0] = 0;
    for (i = 1; i < n; i++) {
        rowptr[i] = nonzeroper[i-1] + rowptr[i-1];
    }

    // Fill values and columns array in parallel
    #pragma omp parallel for private(j) shared(values, cols)
    for (i = 0; i < n; i++) {
        
        int rowNdx = 0;

        for(j = 0; j < n; j++) {
            if (arr[i][j] != 0) {
                // Utilize the row pointer array
                values[rowptr[i]+rowNdx] = 1;
                cols[rowptr[i]+rowNdx] = j;
                rowNdx++;
            }
        }
    }

    gettimeofday(&end2, NULL);
    elapsed2 = end2.tv_sec + end2.tv_usec / 1e6 - start2.tv_sec - start2.tv_usec / 1e6; 
    printf("CSR conversion: %f\n", elapsed2);

    // Print rowptr array
    printf("rowptr: {");
    for (i = 0; i < n; i++) {
        if (i != (n - 1)) {
            printf(" %i,", rowptr[i]);
        } else {
            printf(" %i", rowptr[i]);
        }
    }
    printf("}\n");

    // Print cols array
    printf("cols:   {");
    for (i = 0; i < cnt; i++) {
        if (i != (cnt - 1)) {
            printf(" %i,", cols[i]);
        } else {
            printf(" %i", cols[i]);
        }
    }
    printf("}\n");

    // Print values array
    printf("values: {");
    for (i = 0; i < cnt; i++) {
        if (i != (cnt - 1)) {
            printf(" %i,", values[i]);
        } else {
            printf(" %i", values[i]);
        }
    }
    printf("}\n");

    // Free the memory we allocated
    free(arr);
    free(values);
    free(cols);
    free(rowptr);
    free(nonzeroper);

    // exit
    return 1;
}