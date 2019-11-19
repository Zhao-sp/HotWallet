package com.wallet;

public class JniUtils {
        static {
            System.loadLibrary("jniutils");
        }

        public static native void getbbCourseKeyFromC(Object object);
}
