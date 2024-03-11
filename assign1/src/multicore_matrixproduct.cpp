#include <bits/stdc++.h>
#include <papi.h>
#include <omp.h>

using namespace std;

#define SYSTEMTIME clock_t

double OnMultLineParallelFor(int m_ar, int m_br)
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
	Time1 = clock();
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
	Time2 = clock();
    chrono::duration<double> elapsed_seconds = end - start;
    cout << "Chrono time: " << elapsed_seconds.count() << "s\n";

	double timeTaken = (double)(Time2 - Time1) / CLOCKS_PER_SEC;
	sprintf(st, "Time: %3.3f seconds\n", timeTaken);
	cout << st;

	// display 10 elements of the result matrix to verify correctness
	cout << "Result matrix: " << endl;
	for (i = 0; i < 1; i++)
	{
		for (j = 0; j < min(10, m_br); j++)
			cout << phc[j] << " ";
	}
	cout << endl;

    // chrono::duration<double> elapsed_seconds = end - start;
    // cout << "elapsed time: " << elapsed_seconds.count() << "s\n";

	free(pha);
	free(phb);
	free(phc);
	return timeTaken;
}

double OnMultLineParallel(int m_ar, int m_br)
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

	Time1 = clock();
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
    Time2 = clock();
    auto elapsed_seconds = chrono::duration<double>(end - start);
    cout << "Chrono time: " << elapsed_seconds.count() << "s\n";

	double timeTaken = (double)(Time2 - Time1) / CLOCKS_PER_SEC;
	sprintf(st, "Time: %3.3f seconds\n", timeTaken);
	cout << st;

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
	return timeTaken;
}


int main() {
	int op, lin, col;
    op = 1;

	do
	{
		cout << endl;
		cout << "1. Line Multiplication Parallel version 1" << endl;
        cout << "2. Line Multiplication Parallel version 2" << endl;
		cout << "Selection?: ";
		cin >> op;
		if (op == 0)
			break;
		printf("Dimensions: lins=cols ? ");
		cin >> lin;
		col = lin;

		// Start counting
		// ret = PAPI_start(EventSet);
		// if (ret != PAPI_OK)
		// {
		// 	cout << "ERROR: Start PAPI" << endl;
		// 	handle_error(ret);
		// }

		switch (op)
		{
            case 1:
                OnMultLineParallelFor(lin, col);
                break;
            case 2:
                OnMultLineParallel(lin, col);
                break;
		}

		// ret = PAPI_stop(EventSet, values);
		// if (ret != PAPI_OK)
		// 	cout << "ERROR: Stop PAPI" << endl;
		// printf("L1 DCM: %lld \n", values[0]);
		// printf("L2 DCM: %lld \n", values[1]);

		// ret = PAPI_reset(EventSet);
		// if (ret != PAPI_OK)
		// {
		// 	std::cout << "FAIL reset" << endl;
		// 	handle_error(ret);
		// }

	} while (op != 0);

	// ret = PAPI_remove_event(EventSet, PAPI_L1_DCM);
	// if (ret != PAPI_OK)
	// 	std::cout << "FAIL L1 remove event" << endl;

	// ret = PAPI_remove_event(EventSet, PAPI_L2_DCM);
	// if (ret != PAPI_OK)
	// 	std::cout << "FAIL L2 remove event" << endl;

	// ret = PAPI_destroy_eventset(&EventSet);
	// if (ret != PAPI_OK)
	// 	std::cout << "FAIL destroy" << endl;
}
