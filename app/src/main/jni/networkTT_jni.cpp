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
JNIEXPORT jstring JNICALL Java_hack_com_tantan_JavaUtils_getString
        (JNIEnv *env, jobject, jlong network){

    NetworkTT *networkTT = reinterpret_cast<NetworkTT *>(network);
    if (networkTT != nullptr) {
        //std::string  ret = networkTT->getString();

        char*  ret = networkTT->getString();
        jstring j_str = env->NewStringUTF(ret);


        LOGD("init ====");
        return j_str ;
    }

    return nullptr;
}
}