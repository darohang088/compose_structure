import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.appdistribution)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.example.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties.getProperty("storeFile", "release.jks"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
        getByName("debug") {
            // By default uses the local debug keystore
        }
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("dev") {
            dimension = "environment"
            versionNameSuffix = "-DEV"
            resValue("string", "app_name", "MyApp DEV")
            buildConfigField("String", "BASE_URL", "\"https://dev-api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"DEV\"")
        }
        create("staging") {
            dimension = "environment"
            versionNameSuffix = "-STAGING"
            resValue("string", "app_name", "MyApp Staging")
            buildConfigField("String", "BASE_URL", "\"https://staging-api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"STAGING\"")
        }
        create("preprod") {
            dimension = "environment"
            versionNameSuffix = "-PREPROD"
            resValue("string", "app_name", "MyApp PreProd")
            buildConfigField("String", "BASE_URL", "\"https://preprod-api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"PREPROD\"")
        }
        create("production") {
            dimension = "environment"
            // Production has no suffix
            resValue("string", "app_name", "MyApp")
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"PRODUCTION\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "New automatic deployment from CI"
                
                val credencesFile = rootProject.file("firebase-credentials.json")
                if (credencesFile.exists()) {
                    serviceCredentialsFile = credencesFile.absolutePath
                } else {
                    // Fallbacks: you can also set FIREBASE_TOKEN or GOOGLE_APPLICATION_CREDENTIALS environment variables.
                }
            }
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "New automatic debug deployment from CI"
                
                val credencesFile = rootProject.file("firebase-credentials.json")
                if (credencesFile.exists()) {
                    serviceCredentialsFile = credencesFile.absolutePath
                } else {
                    // Fallbacks: you can also set FIREBASE_TOKEN or GOOGLE_APPLICATION_CREDENTIALS environment variables.
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        // VERY STRICT: Treat all Kotlin warnings as errors natively
        freeCompilerArgs += listOf(
            "-Werror"
        )
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Coil for Images
    implementation(libs.coil.compose)

    // Timber for Logging
    implementation(libs.timber)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
