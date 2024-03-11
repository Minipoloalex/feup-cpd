#include <stdio.h>
#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <time.h>
#include <papi.h>
#include <omp.h>

using namespace std;

typedef vector<int> vi;


#define SYSTEMTIME clock_t

double OnMult(int m_ar, int m_br)
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

	for (i = 0; i < m_ar; i++)
	{
		for (j = 0; j < m_br; j++)
		{
			temp = 0;
			for (k = 0; k < m_ar; k++)
			{
				temp += pha[i * m_ar + k] * phb[k * m_br + j];
			}
			phc[i * m_ar + j] = temp;
		}
	}

	Time2 = clock();
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

// add code here for line x line matriz multiplication
double OnMultLine(int m_ar, int m_br)
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

	Time2 = clock();
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

// add code here for block x block matriz multiplication
double OnMultBlock(int m_ar, int m_br, int bkSize)
{
	return 0;
}

void handle_error(int retval)
{
	printf("PAPI error %d: %s\n", retval, PAPI_strerror(retval));
	// exit(1);
}

void stop_and_reset_papi(int eventSet, long long values[2])
{
	int ret = PAPI_stop(eventSet, values);
	if (ret != PAPI_OK)
	{
		cout << "ERROR: Stop PAPI" << endl;
		handle_error(ret);
	}
	// printf("L1 DCM: %lld \n", values[0]);
	// printf("L2 DCM: %lld \n", values[1]);

	ret = PAPI_reset(eventSet);
	if (ret != PAPI_OK)
	{
		std::cout << "FAIL reset" << endl;
		handle_error(ret);
	}
}

void init_papi()
{
	int retval = PAPI_library_init(PAPI_VER_CURRENT);
	if (retval != PAPI_VER_CURRENT && retval < 0)
	{
		printf("PAPI library version mismatch!\n");
		exit(1);
	}
	if (retval < 0)
		handle_error(retval);

	std::cout << "PAPI Version Number: MAJOR: " << PAPI_VERSION_MAJOR(retval)
			  << " MINOR: " << PAPI_VERSION_MINOR(retval)
			  << " REVISION: " << PAPI_VERSION_REVISION(retval) << "\n";
}

void run_tests(ofstream &ofs, int start, int end, int jump, int eventSet, long long values[2], double (*multMatrices)(int, int))
{
	int ret;
	for (int sz = start; sz <= end; sz += jump)
	{
		ret = PAPI_start(eventSet);
		if (ret != PAPI_OK)
		{
			cout << "ERROR: Start PAPI" << endl;
			handle_error(ret);
		}
		double secondsTaken = multMatrices(sz, sz);
		stop_and_reset_papi(eventSet, values);

		ofs << sz << ',' << secondsTaken << ',' << values[0] << ',' << values[1] << endl;
	}
	ofs << endl;
}

void run_tests_block(ofstream &ofs, int start, int end, int jump, const vi &blockSizes, int eventSet, long long values[2])
{
	int ret;
	ofs << "Block Size,Matrix Size,Time,L1_DCM, L2_DCM\n";
	for (int sz = start; sz <= end; sz += jump)
	{
		for (int bk : blockSizes)
		{
			ret = PAPI_start(eventSet);
			if (ret != PAPI_OK)
			{
				cout << "ERROR: Start PAPI" << endl;
				handle_error(ret);
			}
			double secondsTaken = OnMultBlock(sz, sz, bk);
			stop_and_reset_papi(eventSet, values);

			ofs << sz << ',' << bk << ',' << secondsTaken << ',' << values[0] << ',' << values[1] << '\n';
		}
	}
}

void measure_times(const string &fileName, int eventSet, long long values[2])
{
	ofstream ofs(fileName);
	ofs << "Time,L1_DCM,L2_DCM\n";

	run_tests(ofs, 600, 600, 400, eventSet, values, OnMult);
	cout << "Completed running basic matrix multiplication tests" << endl;

	run_tests(ofs, 600, 3000, 400, eventSet, values, OnMultLine);
	cout << "Completed running matrix line multiplication tests" << endl;

	run_tests(ofs, 4096, 10240, 2048, eventSet, values, OnMultLine);
	cout << "Completed running line mat mul bigger sizes" << endl;

	run_tests_block(ofs, 4096, 10240, 2048, vi({128, 256, 512}), eventSet, values);
	cout << "Completed running block matrix multiplication tests" << endl;
}

int main(int argc, char *argv[])
{
	char c;
	int lin, col, blockSize;
	int op;

	// int EventSet = PAPI_NULL;
	// long long values[2];
	// int ret;

	// init_papi();

	// ret = PAPI_create_eventset(&EventSet);
	// if (ret != PAPI_OK)
	// {
	// 	cout << "ERROR: create eventset" << endl;
	// 	handle_error(ret);
	// }

	// ret = PAPI_add_event(EventSet, PAPI_L1_DCM);
	// if (ret != PAPI_OK)
	// {
	// 	cout << "ERROR: PAPI_L1_DCM" << endl;
	// 	// handle_error(ret);
	// }

	// ret = PAPI_add_event(EventSet, PAPI_L2_DCM);
	// if (ret != PAPI_OK)
	// {
	// 	cout << "ERROR: PAPI_L2_DCM" << endl;
	// 	handle_error(ret);
	// }

	if (argc > 1)
	{
		// measure_times(argv[1], EventSet, values);
		return 0;
	}

	op = 1;
	do
	{
		cout << endl
			 << "1. Multiplication" << endl;
		cout << "2. Line Multiplication" << endl;
		cout << "3. Block Multiplication" << endl;
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
			OnMult(lin, col);
			break;
		case 2:
			OnMultLine(lin, col);
			break;
		case 3:
			cout << "Block Size? ";
			cin >> blockSize;
			OnMultBlock(lin, col, blockSize);
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
