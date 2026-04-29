# CMake and NDK in Android: Understanding the Flow

## What is CMake?
CMake is a cross-platform build tool used in Android to compile native C and C++ code into shared libraries (`.so` files) that can be bundled with your APK. The Android Native Development Kit (NDK) provides the necessary toolchains to make this happen.

## Why Use Native C/C++ in Android?
1. **Security:** Compiled C/C++ binaries (`.so` files) are much harder to decompile and reverse-engineer than Kotlin/Java bytecode. This makes it a great place to store API keys, encryption secrets, or proprietary algorithms (like Biometric signature validation).
2. **Performance:** Computationally intensive tasks (like image processing, machine learning, or physics engines) run faster in native code.
3. **Reusability:** If you have an existing C/C++ library, you don't need to rewrite it in Kotlin.

---

## The Execution Flow

When your Android app interacts with C++ code, the flow relies on the **Java Native Interface (JNI)**, which acts as a bridge between the JVM (Kotlin) and the native layer (C++).

### 1. Kotlin Layer (The Bridge)
In your Kotlin code, you declare a function using the `external` keyword. This tells the JVM that the implementation of this function exists in a native library.
```kotlin
object NativeBridge {
    init {
        // Loads the compiled C++ library (secure-lib.so)
        System.loadLibrary("secure-lib") 
    }

    // Function declaration, implementation is in C++
    external fun getSecureSecret(): String
}
```

### 2. JNI Layer (The Handshake)
The C++ file must define the function using specific JNI naming conventions so the JVM can find it. The naming convention is typically:
`Java_package_name_ClassName_methodName`

### 3. C++ Layer (The Implementation)
The actual logic runs in C++. It receives parameters from Kotlin, processes them natively, and returns the result back across the bridge.
```cpp
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_app_utils_NativeBridge_getSecureSecret(JNIEnv* env, jobject /* this */) {
    std::string secret = "SuperSecretKeyFromNative_12345";
    // Convert C++ string back to Kotlin-compatible String
    return env->NewStringUTF(secret.c_str());
}
```

---

## The Build Flow (How it compiles)

1. **Gradle Build:** You run `./gradlew assembleDebug`.
2. **Gradle triggers CMake:** Gradle sees the `externalNativeBuild` block in your `build.gradle.kts` and hands control to CMake.
3. **CMake reads CMakeLists.txt:** CMake looks at `app/src/main/cpp/CMakeLists.txt` to understand what C++ files to compile and what system libraries (like the `log` library) to link.
4. **Compilation:** The NDK compiler (Clang) compiles the `.cpp` files for the specific architectures defined in your `abiFilters` (e.g., `arm64-v8a` for modern phones, `x86_64` for emulators).
5. **Packaging:** The resulting `.so` (Shared Object) files are placed inside the final APK.

## Where to find the files in this project?
- **Build configuration:** `app/build.gradle.kts` (Look for `externalNativeBuild`)
- **CMake Instructions:** `app/src/main/cpp/CMakeLists.txt`
- **C++ Source Code:** `app/src/main/cpp/secure-lib.cpp`
- **Kotlin Bridge:** `app/src/main/java/com/example/app/utils/NativeBridge.kt`
