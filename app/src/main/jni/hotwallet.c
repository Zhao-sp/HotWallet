//
// Created by Lenovo on 2019/11/18.
//
#include "com_wallet_utils_JniUtils.h"
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <Android/log.h>
#define TAG "result" // 这个是自定义的LOG的标识
#define LOGE(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
//C++实现

const char *app_signature_sha1="6C2C375774EBE37E93B2F13D28BD84E9E0F83525";
const char HexCode[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
JNIEXPORT void JNICALL Java_com_wallet_utils_JniUtils_getbbCourseKeyFromC
        (JNIEnv *env, jclass clz, jobject context_object){
    jclass context_class = (*env)->GetObjectClass(env,context_object);

    //context.getPackageManager()
    jmethodID methodId = (*env)->GetMethodID(env,context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject package_manager_object = (*env)->CallObjectMethod(env,context_object, methodId);
    if (package_manager_object == NULL) {
       LOGE("getPackageManager() Failed!");
        return;
    }

    //context.getPackageName()
    methodId = (*env)->GetMethodID(env,context_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name_string = (jstring)(*env)->CallObjectMethod(env,context_object, methodId);
    if (package_name_string == NULL) {
       LOGE("getPackageName() Failed!");
        return ;
    }
    (*env)->DeleteLocalRef(env,context_class);

    //PackageManager.getPackageInfo(Sting, int)
    //public static final int GET_SIGNATURES= 0x00000040;
    jclass pack_manager_class = (*env)->GetObjectClass(env,package_manager_object);
    methodId = (*env)->GetMethodID(env,pack_manager_class, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    (*env)->DeleteLocalRef(env,pack_manager_class);
    jobject package_info_object = (*env)->CallObjectMethod(env,package_manager_object, methodId, package_name_string, 0x40);
    if (package_info_object == NULL) {
        LOGE("getPackageInfo() Failed!");
        return ;
    }
    (*env)->DeleteLocalRef(env,package_manager_object);

    //PackageInfo.signatures[0]
    jclass package_info_class = (*env)->GetObjectClass(env,package_info_object);
    jfieldID fieldId = (*env)->GetFieldID(env,package_info_class, "signatures", "[Landroid/content/pm/Signature;");
    (*env)->DeleteLocalRef(env,package_info_class);
    jobjectArray signature_object_array = (jobjectArray)(*env)->GetObjectField(env,package_info_object, fieldId);
    if (signature_object_array == NULL) {
        LOGE("PackageInfo.signatures[] is null");
        return ;
    }
    jobject signature_object = (*env)->GetObjectArrayElement(env,signature_object_array, 0);
    (*env)->DeleteLocalRef(env,package_info_object);

    //Signature.toByteArray()
    jclass signature_class = (*env)->GetObjectClass(env,signature_object);
    methodId = (*env)->GetMethodID(env,signature_class, "toByteArray", "()[B");
    (*env)->DeleteLocalRef(env,signature_class);
    jbyteArray signature_byte = (jbyteArray) (*env)->CallObjectMethod(env,signature_object, methodId);

    //new ByteArrayInputStream
    jclass byte_array_input_class=(*env)->FindClass(env,"java/io/ByteArrayInputStream");
    methodId=(*env)->GetMethodID(env,byte_array_input_class,"<init>","([B)V");
    jobject byte_array_input=(*env)->NewObject(env,byte_array_input_class,methodId,signature_byte);

    //CertificateFactory.getInstance("X.509")
    jclass certificate_factory_class=(*env)->FindClass(env,"java/security/cert/CertificateFactory");
    methodId=(*env)->GetStaticMethodID(env,certificate_factory_class,"getInstance","(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;");
    jstring x_509_jstring=(*env)->NewStringUTF(env,"X.509");
    jobject cert_factory=(*env)->CallStaticObjectMethod(env,certificate_factory_class,methodId,x_509_jstring);

    //certFactory.generateCertificate(byteIn);
    methodId=(*env)->GetMethodID(env,certificate_factory_class,"generateCertificate",("(Ljava/io/InputStream;)Ljava/security/cert/Certificate;"));
    jobject x509_cert=(*env)->CallObjectMethod(env,cert_factory,methodId,byte_array_input);
    (*env)->DeleteLocalRef(env,certificate_factory_class);

    //cert.getEncoded()
    jclass x509_cert_class=(*env)->GetObjectClass(env,x509_cert);
    methodId=(*env)->GetMethodID(env,x509_cert_class,"getEncoded","()[B");
    jbyteArray cert_byte=(jbyteArray)(*env)->CallObjectMethod(env,x509_cert,methodId);
    (*env)->DeleteLocalRef(env,x509_cert_class);

    //MessageDigest.getInstance("SHA1")
    jclass message_digest_class=(*env)->FindClass(env,"java/security/MessageDigest");
    methodId=(*env)->GetStaticMethodID(env,message_digest_class,"getInstance","(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jstring sha1_jstring=(*env)->NewStringUTF(env,"SHA1");
    jobject sha1_digest=(*env)->CallStaticObjectMethod(env,message_digest_class,methodId,sha1_jstring);

    //sha1.digest (certByte)
    methodId=(*env)->GetMethodID(env,message_digest_class,"digest","([B)[B");
    jbyteArray sha1_byte=(jbyteArray)(*env)->CallObjectMethod(env,sha1_digest,methodId,cert_byte);
    (*env)->DeleteLocalRef(env,message_digest_class);

    //toHexString
    jsize array_size=(*env)->GetArrayLength(env,sha1_byte);
    jbyte* sha1 =(*env)->GetByteArrayElements(env,sha1_byte,NULL);
    char hex_sha[array_size*2+1];
    for (int i = 0; i <array_size ; ++i) {
        hex_sha[2*i]=HexCode[((unsigned char)sha1[i])/16];
        hex_sha[2*i+1]=HexCode[((unsigned char)sha1[i])%16];
    }
    hex_sha[array_size*2]='\0';
    //比较签名
    if (strcmp(hex_sha,app_signature_sha1)==0){
       LOGE("验证通过");
    } else{
       LOGE("验证失败");
       jclass temp_clazz = NULL;
       jmethodID mid_static_method;
       // 1、从classpath路径下搜索ClassMethod这个类，并返回该类的Class对象
       temp_clazz =(*env)->FindClass(env,"java/lang/System");
       mid_static_method = (*env)->GetStaticMethodID(env,temp_clazz,"exit","(I)V");
       (*env)->CallStaticVoidMethod(env,temp_clazz,mid_static_method,0);
       (*env)->DeleteLocalRef(env,temp_clazz);
    }
    return ;
}
