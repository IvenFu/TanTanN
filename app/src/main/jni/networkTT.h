//
// Created by Iven on 2019-07-26.
//

#ifndef TANTAN_PROBE_H
#define TANTAN_PROBE_H

#include <string>


class NetworkTT {

public:
    NetworkTT();
    ~NetworkTT();

    void  init ();
    void  uninit();
    //std::string getString();

    char* getString();

    //ø™∆Ù∂™∞¸¬ RTTÃΩ≤‚
    int ProbingLostrateAndRTT();

//ªÒ»°Ω·π˚
    int GetLostrateAndRTT(unsigned int* pRttMs, float* pfLossRate);

//ø™∆Ù…œ––¥¯øÌÃΩ≤‚
    int ProbingUpBw();

//ªÒ»°Ω·π˚
    int GetUpBw(int* pBitrate);

//ø™∆Ùœ¬––¥¯øÌÃΩ≤‚
    int ProbingDownBw();

//ªÒ»°Ω·π˚
    int GetDownBw(int* pBitrate);

};

#endif //TANTAN_PROBE_H
