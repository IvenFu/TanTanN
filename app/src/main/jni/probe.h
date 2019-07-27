#ifndef _PROBE_H_
#define _PROBE_H_

#if defined(WIN32)

#else

#ifndef __stdcall
#define __stdcall
#endif

#endif

//开启上行丢包率RTT探测
int ProbingLostrateAndRTT(char* szServerIp);

//获取结果
int GetRTT(unsigned int* pRttMs);
int GetLostrate(float* pfLossRate);

//开启下行丢包率探测
int ProbingDownLostrate(char* szServerIp);
int GetDownLostrate(float* pfLossRate);

//开启上行带宽探测
int ProbingUpBw(char* szServerIp);

//获取结果
int GetUpBw(int* pBitrate);

//开启下行带宽探测
int ProbingDownBw(char* szServerIp);

//获取结果
int GetDownBw(int* pBitrate);

#endif