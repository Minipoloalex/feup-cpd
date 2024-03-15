CC = g++
CFLAGS = -fopenmp -lpapi
TARGET = multicore_matrixproduct
SRC = multicore_matrixproduct.cpp



clean:
	rm -f $(TARGET) matrix_product


multicore:
	$(CC) -O2 $(SRC) -o $(TARGET) $(CFLAGS)

run_multicore:
	make multicore && ./multicore_matrixproduct

normal:
	$(CC) -O2 matrix_product.cpp -o matrix_product -lpapi

run_normal:
	make normal && ./matrix_product

run_normal_tests:
	make normal && ./matrix_product $$(date +%s%N)
