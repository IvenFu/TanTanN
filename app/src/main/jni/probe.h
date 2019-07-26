#ifndef _PROBE_H_
#define _PROBE_H_

#if defined(WIN32)

#else

#ifndef __stdcall
#define __stdcall
#endif

#endif

//����������RTT̽��
int ProbingLostrateAndRTT(char* szServerIp);

//��ȡ���
int GetRTT(unsigned int* pRttMs);

int GetLostrate(float* pfLossRate);

//�������д���̽��
int ProbingUpBw(char* szServerIp);

//��ȡ���
int GetUpBw(int* pBitrate);

//�������д���̽��
int ProbingDownBw(char* szServerIp);

//��ȡ���
int GetDownBw(int* pBitrate);

#endif