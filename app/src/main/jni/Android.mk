LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS    := -lm -llog
LOCAL_MODULE := jniutils
LOCAL_SRC_FILES := hotwallet.c
include $(BUILD_SHARED_LIBRARY)