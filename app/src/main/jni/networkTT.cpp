//
// Created by Iven on 2019-07-26.
//

#include "networkTT.h"

#include  <android/log.h>
#include <jni.h>
#include "probe.h"


#define TAG    "Network"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__) // 定义LOGD类型


NetworkTT::NetworkTT() {
}

NetworkTT::~NetworkTT() {

    LOGD( " ~~NetworkTT  ");

}

void NetworkTT::init() {
    LOGD("network init");
    return ;
}


void NetworkTT::uninit() {
    LOGD("network uninit");
    return ;
}

int NetworkTT::Probing_Lostrate_And_RTT(const char* probeIP){

    char *ipChar = const_cast<char *> (probeIP);
    ProbingLostrateAndRTT(ipChar);
    return 0;
}
unsigned int NetworkTT::Get_RTT(){

    unsigned int  rtt = 0 ;
    GetRTT(&rtt);

    LOGD("Get_RTT %d ", rtt) ;
    return rtt ;
}

float NetworkTT::Get_Lossrate(){

    float lossRate = 0;
    GetLostrate(&lossRate);
    return lossRate;
}

//ø™∆Ù…œ––¥¯øÌÃΩ≤‚
int NetworkTT::Probing_Up_Bw(const char* probeIP){

    char *ipChar = const_cast<char *> (probeIP);

    ProbingUpBw(ipChar);

    return 0;
}

//ªÒ»°Ω·π˚
int NetworkTT::Get_Up_Bw(){

    int  upBw = 0;
    GetUpBw(&upBw);
    return upBw;
}

//ø™∆Ùœ¬––¥¯øÌÃΩ≤‚
int NetworkTT::Probing_Down_Bw(const char* probeIP){

    char *ipChar = const_cast<char *> (probeIP);

    ProbingDownBw(ipChar);

    return 0;
}

//ªÒ»°Ω·π˚
int NetworkTT::Get_Down_Bw(){
    int  downBw = 0;
    GetUpBw(&downBw);
    return downBw;
}

int NetworkTT::Probing_Down_Lossrate(const char* probeIP){
    char *ipChar = const_cast<char *> (probeIP);

    ProbingDownLostrate(ipChar);

    return 0;
}

float NetworkTT::Get_Down_Lossrate(){
    float  downLossrate = 0;

    GetDownLostrate(&downLossrate);
    return downLossrate;
}

