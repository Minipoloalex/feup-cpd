#include <bits/stdc++.h>
#include <papi.h>
#include <omp.h>

using namespace std;

#define SYSTEMTIME clock_t

double OnMultLineParallelOuterFor(int m_ar, int m_br)
{
	SYSTEMTIME Time1, Time2;

	char st[100];
	double temp;
	int i, j, k;

	double *pha, *phb, *phc;

	pha = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phb = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phc = (double *)malloc((m_ar * m_ar) * sizeof(double));

	for (i = 0; i < m_ar; i++)
		for (j = 0; j < m_ar; j++)
			pha[i * m_ar + j] = (double)1.0;

	for (i = 0; i < m_br; i++)
		for (j = 0; j < m_br; j++)
			phb[i * m_br + j] = (double)(i + 1);

    double dif;
    auto start = chrono::system_clock::now();

	#pragma omp parallel for private(j, k)
	for (i = 0; i < m_ar; i++)
	{
		for (k = 0; k < m_br; k++)
		{
			for (j = 0; j < m_ar; j++)
			{
				phc[i * m_ar + j] += pha[i * m_ar + k] * phb[k * m_br + j];
			}
		}
	}

    auto end = chrono::system_clock::now();
	auto elapsed_seconds = chrono::duration<double>(end - start).count();
    cout << "Chrono time: " << elapsed_seconds << "s\n";

	// display 10 elements of the result matrix to verify correctness
	cout << "Result matrix: " << endl;
	for (i = 0; i < 1; i++)
	{
		for (j = 0; j < min(10, m_br); j++)
			cout << phc[j] << " ";
	}
	cout << endl;

	free(pha);
	free(phb);
	free(phc);
	return elapsed_seconds;
}

double OnMultLineParallelInnerFor(int m_ar, int m_br)
{
	SYSTEMTIME Time1, Time2;

	char st[100];
	double temp;
	int i, j, k;

	double *pha, *phb, *phc;

	pha = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phb = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phc = (double *)malloc((m_ar * m_ar) * sizeof(double));

	for (i = 0; i < m_ar; i++)
		for (j = 0; j < m_ar; j++)
			pha[i * m_ar + j] = (double)1.0;

	for (i = 0; i < m_br; i++)
		for (j = 0; j < m_br; j++)
			phb[i * m_br + j] = (double)(i + 1);

    auto start = chrono::system_clock::now();

	#pragma omp parallel private(i, k)
	for (i = 0; i < m_ar; i++)
	{
		for (k = 0; k < m_br; k++)
		{
			#pragma omp for     // the threads synchronize here
            for (j = 0; j < m_ar; j++)
			{
				phc[i * m_ar + j] += pha[i * m_ar + k] * phb[k * m_br + j];
			}
		}
	}
    auto end = chrono::system_clock::now();
	auto elapsed_seconds = chrono::duration<double>(end - start).count();
    cout << "Chrono time: " << elapsed_seconds << "s\n";

	// display 10 elements of the result matrix to verify correctness
	cout << "Result matrix: " << endl;
	for (i = 0; i < 1; i++)
	{
		for (j = 0; j < min(10, m_br); j++)
			cout << phc[j] << " ";
	}
	cout << endl;

	free(pha);
	free(phb);
	free(phc);
	return elapsed_seconds;
}

void runFunctionTests(ofstream &ofs, double (*f)(int, int)) {
	ofs << "matrix_size" << "," << "time" << "," << "num_threads" << endl;
	cout << "Function running" << endl;
	cout << endl;
	for (int mx_size = 600; mx_size <= 3000; mx_size += 400) {
		double timeTaken = f(mx_size, mx_size);
		ofs << mx_size << "," << timeTaken << "," << omp_get_num_procs() << endl;
		cout << "Ran for size " << mx_size << endl;
	}
	for (int mx_size = 4096; mx_size <= 10240; mx_size += 2048) {
		double timeTaken = f(mx_size, mx_size);
		ofs << mx_size << "," << timeTaken << "," << omp_get_num_procs() << endl;
		cout << "Ran for size " << mx_size << endl;
	}
}


void runTests(const string &filename) {
	ofstream ofs(filename);
	// runFunctionTests(ofs, OnMultLineParallelOuterFor);
	runFunctionTests(ofs, OnMultLineParallelInnerFor);
}


int main(int argc, char *argv[]) {
	if (argc > 1) {
		runTests(argv[1]);
		return 0;
	}
	int op, lin, col;
    op = 1;

	do
	{
		cout << endl;
		cout << "1. Line Multiplication Parallel version 1" << endl;
        cout << "2. Line Multiplication Parallel version 2" << endl;
		cout << "3. Run tests" << endl;
		cout << "Selection?: ";
		cin >> op;
		if (op == 0)
			break;

		if (op == 1 || op == 2) {
			printf("Dimensions: lins=cols ? ");
			cin >> lin;
			col = lin;
		}

		switch (op)
		{
            case 1:
                OnMultLineParallelOuterFor(lin, col);
                break;
            case 2:
                OnMultLineParallelInnerFor(lin, col);
                break;
			case 3:
				runTests("out" + to_string(time(0)) + ".txt");
				break;
		}
	} while (op != 0);
}
