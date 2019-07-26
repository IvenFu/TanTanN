//
// Created by Iven on 2019-07-26.
//

#include "networkTT.h"

#include  <android/log.h>
#include <jni.h>
#include <string>


#define TAG    "Network"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__) // 定义LOGD类型


NetworkTT::NetworkTT() {
    LOGD("NetworkTT");
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

char* NetworkTT::getString() {
    LOGD("network getString");
    char*  ret = " NetworkTT::getString()" ;

    return ret ;
}

//ø™∆Ù∂™∞¸¬ RTTÃΩ≤‚
int NetworkTT::ProbingLostrateAndRTT(){

    return 0;
}

//ªÒ»°Ω·π˚
int NetworkTT::GetLostrateAndRTT(unsigned int* pRttMs, float* pfLossRate){

    return 0;
}

//ø™∆Ù…œ––¥¯øÌÃΩ≤‚
int NetworkTT::ProbingUpBw(){
    return 0;
}

//ªÒ»°Ω·π˚
int NetworkTT::GetUpBw(int* pBitrate){

    return 0;
}

//ø™∆Ùœ¬––¥¯øÌÃΩ≤‚
int NetworkTT::ProbingDownBw(){
    return 0;
}

//ªÒ»°Ω·π˚
int NetworkTT::GetDownBw(int* pBitrate){
    return 0;
}