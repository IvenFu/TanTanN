//
// Created by Iven on 2019-07-26.
//

#ifndef TANTAN_PROBE_H
#define TANTAN_PROBE_H


class NetworkTT {

public:
    NetworkTT();
    ~NetworkTT();

    void  init ();
    void  uninit();
    //std::string getString();

    char* getString();

    //ø™∆Ù∂™∞¸¬ RTTÃΩ≤‚
    int Probing_Lostrate_And_RTT(const char * probIP);

     unsigned  int Get_RTT();

    float  Get_Lossrate();


//ø™∆Ù…œ––¥¯øÌÃΩ≤‚
    int Probing_Up_Bw(const char * probIP);

//ªÒ»°Ω·π˚
    int Get_Up_Bw();

//ø™∆Ùœ¬––¥¯øÌÃΩ≤‚
    int Probing_Down_Bw(const char * probIP);

//ªÒ»°Ω·π˚
    int Get_Down_Bw();

};

#endif //TANTAN_PROBE_H
