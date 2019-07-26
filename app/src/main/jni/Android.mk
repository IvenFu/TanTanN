LOCAL_PATH := $(call my-dir) #指定cpp文件位置

include $(CLEAR_VARS) #编译时清除旧库

LOCAL_MODULE    := libjnitest #生成so的名字，前面加lib

#需要编译的cpp文件
LOCAL_SRC_FILES := networkTT_jni.cpp\
                   networkTT.cpp

LOCAL_C_INCLUDES := networkTT.h \
                    probe.h


#LOCAL_PREBUILT_LIBS += libProbing:libs/armeabi-v7a/libProbing.so
#LOCAL_MODULE_TAGS := optional
#include $(BUILD_MULTI_PREBUILT)

#LOCAL_SHARED_LIBRARIES := libNPQos \
#                          libProbing

LOCAL_LDLIBS := -lProbing -lm -llog

LOCAL_LDLIBS += -L$(LOCAL_PATH)/../../../libs/armeabi-v7a

#LOCAL_LDLIBS += -L/Users/iven/Project/Hacker/tantan/app/libs/armeabi-v7a
>>>>>>> 01ac90bf0332785ffd8fc1ac748841648669dc27

include $(BUILD_SHARED_LIBRARY) #注明生成动态库
