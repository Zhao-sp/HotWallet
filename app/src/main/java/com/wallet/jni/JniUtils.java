package com.wallet.jni;

public class JniUtils {
    public static native String getStringC();

    public static native String myEncrypt(String content);

    public static native String myDecrypt(String content);
}
