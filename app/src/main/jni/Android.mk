LOCAL_PATH := $(call my-dir) #指定cpp文件位置

include $(CLEAR_VARS) #编译时清除旧库

LOCAL_MODULE    := libjnitest #生成so的名字，前面加lib
LOCAL_SRC_FILES := jnitest.cpp #需要编译的cpp文件



LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY) #注明生成动态库
