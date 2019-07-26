//
// Created by Iven on 2019-07-25.
//

#include <jni.h>
#include "hack_com_tantan_JavaUtils.h"

extern "C" {
JNIEXPORT jstring JNICALL Java_hack_com_tantan_JavaUtils_getString
  (JNIEnv *env, jclass jc){

    const char* ch = "String From JNI";
    return env->NewStringUTF(ch);
  }

}