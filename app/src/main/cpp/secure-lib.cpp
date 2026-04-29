#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "NativeSecureLib"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_app_utils_NativeBridge_getSecureSecret(
        JNIEnv* env,
        jobject /* this */) {
    LOGD("getSecureSecret called from Kotlin");
    
    // In a real application, this could perform some secure operations
    // or return a secret key that is harder to extract from a C++ compiled binary
    // compared to Kotlin bytecode.
    std::string secret = "SuperSecretKeyFromNative_12345";
    
    return env->NewStringUTF(secret.c_str());
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_app_utils_NativeBridge_validateBiometricSignature(
        JNIEnv* env,
        jobject /* this */,
        jbyteArray signature) {
    
    LOGD("validateBiometricSignature called");
    
    if (signature == nullptr) {
        LOGE("Signature is null");
        return JNI_FALSE;
    }

    // Get length of the byte array
    jsize length = env->GetArrayLength(signature);
    if (length == 0) {
        LOGE("Signature is empty");
        return JNI_FALSE;
    }

    // Access the bytes
    jbyte* bytes = env->GetByteArrayElements(signature, nullptr);
    if (bytes == nullptr) {
        LOGE("Failed to get byte array elements");
        return JNI_FALSE;
    }

    // Example logic: Just log the length and pretend it's valid
    LOGD("Validating signature of length %d", length);
    bool isValid = true; 

    // Release the byte array
    // JNI_ABORT tells JNI not to copy back the changes if we modified it
    env->ReleaseByteArrayElements(signature, bytes, JNI_ABORT);

    return isValid ? JNI_TRUE : JNI_FALSE;
}
