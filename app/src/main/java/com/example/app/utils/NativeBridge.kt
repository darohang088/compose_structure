package com.example.app.utils

/**
 * A bridge class to interact with native C++ code via JNI.
 */
object NativeBridge {

    // Used to load the 'secure-lib' library on application startup.
    init {
        try {
            System.loadLibrary("secure-lib")
        } catch (e: UnsatisfiedLinkError) {
            // In a production app, you might want to log this to Crashlytics
            // as this indicates the native library wasn't found or couldn't be loaded
            // (e.g. running on an unsupported architecture).
            e.printStackTrace()
        }
    }

    /**
     * A native method that is implemented by the 'secure-lib' native library,
     * which is packaged with this application.
     * 
     * @return A secure string defined in native C++ code.
     */
    external fun getSecureSecret(): String

    /**
     * Example method to validate biometric signatures in native code 
     * where it's harder to tamper with than Kotlin bytecode.
     * 
     * @param signature The biometric signature byte array.
     * @return true if valid, false otherwise.
     */
    external fun validateBiometricSignature(signature: ByteArray): Boolean
}
