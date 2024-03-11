CC = g++
CFLAGS = -fopenmp -lpapi
TARGET = multicore_matrixproduct
SRC = multicore_matrixproduct.cpp

multicore:
	$(CC) -O2 $(SRC) -o $(TARGET) $(CFLAGS)

clean:
	rm -f $(TARGET)
