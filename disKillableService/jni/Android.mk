LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)

LOCAL_MODULE     := com_example_jnitest_NativeTest
LOCAL_LDLIBS+= -L$(SYSROOT)/usr/lib -llog
LOCAL_SRC_FILES  := com_example_jnitest_NativeTest.c
include $(BUILD_SHARED_LIBRARY)
