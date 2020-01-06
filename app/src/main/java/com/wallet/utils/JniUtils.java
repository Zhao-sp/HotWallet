package com.wallet.utils;

public class JniUtils {
        static {
            System.loadLibrary("jniutils");
        }

        public static native void getbbCourseKeyFromC(Object object);
}
