LOCAL_PATH := $(call my-dir) #指定cpp文件位置

include $(CLEAR_VARS) #编译时清除旧库

LOCAL_MODULE    := libjnitest #生成so的名字，前面加lib

#需要编译的cpp文件
LOCAL_SRC_FILES := networkTT_jni.cpp\
                   networkTT.cpp

LOCAL_C_INCLUDES := networkTT.h

#LOCAL_STATIC_LIBRARIES := libjson \
#                           libjsoncpp

LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY) #注明生成动态库
