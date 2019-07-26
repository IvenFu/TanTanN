//
// Created by Iven on 2019-07-25.
//

#include <jni.h>
#include <android/log.h>
#include<stdio.h>
#include <string>
#include "hack_com_tantan_JavaUtils.h"
#include "networkTT.h"


#define TAG    "network_jni"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__) // 定义LOGD类型

extern "C" {
JNIEXPORT jlong JNICALL Java_hack_com_tantan_JavaUtils_createNetwork
        (JNIEnv *, jobject, jlong network){

    NetworkTT *networkTT  = new NetworkTT();

    jlong ret = reinterpret_cast<intptr_t>(networkTT);

    LOGD("createNetwork"," init " );
    return ret;
}
}

extern "C" {
JNIEXPORT void JNICALL Java_hack_com_tantan_JavaUtils_init
        (JNIEnv *, jobject, jlong network){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {
        networkTT->init();
        LOGD("init ====");
    }

    return ;
}
}

extern "C" {
JNIEXPORT void JNICALL Java_hack_com_tantan_JavaUtils_uninit
        (JNIEnv *env, jobject, jlong network){



    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {
        networkTT->uninit();
        LOGD("uninit ====");
        delete  networkTT;
    }

    return;
}
}

extern "C" {
JNIEXPORT jint JNICALL Java_hack_com_tantan_JavaUtils_ProbingLostrateAndRTT
        (JNIEnv *env, jobject , jlong network, jstring probeIP){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {

        const char *charIP = env->GetStringUTFChars(probeIP, nullptr);
        networkTT->Probing_Lostrate_And_RTT(charIP);

        LOGD("ProbingLostrateAndRTT ");

        return 0;
    }

    LOGD("ProbingLostrateAndRTT failed" );

    return -1;

}
}

extern "C" {
JNIEXPORT jint JNICALL Java_hack_com_tantan_JavaUtils_GetRTT
        (JNIEnv *env, jobject,jlong network){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {

        int retRTT = networkTT->Get_RTT();

        LOGD("GetRTT ");

        return retRTT;
    }

    LOGD("GetRTT failed ");

    return -1;

}
}


extern "C" {
JNIEXPORT jfloat JNICALL Java_hack_com_tantan_JavaUtils_GetLossrate
        (JNIEnv *env, jobject,jlong network){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {

        float retLossrate = networkTT->Get_Lossrate();

        LOGD("GetLossrate ");

        return retLossrate;
    }

    LOGD("GetLossrate failed ");

    return -1;

}
}


extern "C" {
JNIEXPORT jint JNICALL Java_hack_com_tantan_JavaUtils_ProbingUpBw
        (JNIEnv *env, jobject, jlong network, jstring probeIP) {

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {
        const char *charIP = env->GetStringUTFChars(probeIP, nullptr);
        networkTT->Probing_Up_Bw(charIP);
    }

    return  -1;
}
}

extern "C" {
JNIEXPORT jint JNICALL Java_hack_com_tantan_JavaUtils_GetUpBw
        (JNIEnv *env, jobject,jlong network){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {

        float retUpBW = networkTT->Get_Up_Bw();

        LOGD("GetUpBw ");

        return retUpBW;
    }

    LOGD("retUpBW failed ");

    return -1;

}
}

extern "C" {
JNIEXPORT jint JNICALL Java_hack_com_tantan_JavaUtils_ProbingDownBw
        (JNIEnv *env, jobject, jlong network, jstring probeIP) {

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {
        const char *charIP = env->GetStringUTFChars(probeIP, nullptr);
        networkTT->Probing_Up_Bw(charIP);
    }

    return -1;
}
}

extern "C" {
JNIEXPORT jint JNICALL Java_hack_com_tantan_JavaUtils_GetDownBw
        (JNIEnv *env, jobject,jlong network){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {

        float retDownBW = networkTT->Get_Down_Bw();

        LOGD("GetDownBw ");

        return retDownBW;
    }

    LOGD("retDownBW failed ");

    return -1;

}
}