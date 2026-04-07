# Android Environment & Quality Implementation Guide

This document sequentially outlines the exact configuration blocks that need to be injected into your Gradle ecosystem to completely reconstruct the robust **Multi-Environment Build Matrix**, **Strict Kotlin Compilation Rules**, and **Detekt Static Analysis** that drives this project.

If you ever need to set this up from scratch, follow these instructions step-by-step.

---

## 1. Version Catalog (`gradle/libs.versions.toml`)

Before declaring plugins in your module, you must map the `detekt` static analysis tool globally inside your version catalog.

```toml
[versions]
# ... your other versions ...
detekt = "1.23.4"

[plugins]
# ... your other plugins ...
detekt = { id = "io.gitlab.arturbosch.detekt", version.ref = "detekt" }
```

---

## 2. Project-Level `build.gradle.kts`

Apply the Detekt plugin at the absolute root of the project to ensure rules funnel downwards into all feature modules, and wire the formatting plugin dependency so auto-formatting checks work.

```kotlin
// Top-level build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.detekt) // Inject Detekt globally
}

// Hook detekt formatting rules
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4")
}
```

---

## 3. App-Level `build.gradle.kts`

This is the core engine configuration. It enforces the four primary environments (`dev`, `staging`, `preprod`, `production`), links your cryptographic signing keys, and configures the strict native `-Werror` compile rules.

### A. Load External Keystore Data
At the very top of `app/build.gradle.kts` (before the `android {}` block), extract your signing configs safely from `keystore.properties` rather than hardcoding them into public version control.

```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
}
```

### B. Configure Signing & Build Types
Inside `android {}`, map your secure signing config and strictly apply it to the `release` Build Type.

```kotlin
android {
    // ... namespace, compileSdk ...

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String? ?: "release.jks")
            storePassword = keystoreProperties["storePassword"] as String?
            keyAlias = keystoreProperties["keyAlias"] as String?
            keyPassword = keystoreProperties["keyPassword"] as String?
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }
```

### C. Implement the Flavor Matrix
Right below `buildTypes`, construct the isolated dimensions. Each environment maps its distinct backend API URL and distinct Application ID namespace so they can be installed side-by-side on device.

```kotlin
    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-DEV"
            buildConfigField("String", "BASE_URL", "\"https://dev-api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"Dev\"")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-STG"
            buildConfigField("String", "BASE_URL", "\"https://staging-api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"Staging\"")
        }
        create("preprod") {
            dimension = "environment"
            applicationIdSuffix = ".preprod"
            versionNameSuffix = "-PRE"
            buildConfigField("String", "BASE_URL", "\"https://preprod-api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"PreProd\"")
        }
        create("production") {
            dimension = "environment"
            // Production retains the pure namespace (e.g., com.example.app)
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField("String", "ENVIRONMENT_NAME", "\"Production\"")
        }
    }
```

### D. Enforce Native Quality Rules (-Werror)
Force the Kotlin Compiler to immediately kill the build if there are any unused parameters, unused imports, or dead code mappings. This is placed at the bottom of the file.

```kotlin
    // Enable BuildConfig generation so we can read the constants above
    buildFeatures {
        buildConfig = true
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
        
        // ❗ STRICT COMPILATION ❗
        // Treats all warnings as fatal errors (Unused imports, dead variables, etc)
        freeCompilerArgs += listOf(
            "-Werror"
        )
    }
} // End of android block
```

---

## 4. Detekt Task Configuration
Also in `app/build.gradle.kts`, configure Detekt to point towards your `detekt.yml` file, enforcing custom rules (such as explicitly banning `android.util.Log` inside code).

```kotlin
// Add this completely outside the android block
detekt {
    // Generate native compiler mappings so Detekt knows exactly what went wrong
    buildUponDefaultConfig = true 
    allRules = false 
    // Point this to your strict rulebook
    config.setFrom(files("$rootDir/detekt.yml"))
}

// Bind detekt execution sequentially before native gradle compilation runs
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true) // Generates a clean HTML report inside /build
    }
}
```

---

## 5. Required Root Files
For the above gradle architectures to function properly, you must have the following configuration files resting securely in your absolute root directory:

1. **`keystore.properties`**  *(Contains PKCS12 compatible mapped string references to passwords)*. Should be `.gitignore`'d.
2. **`detekt.yml`** *(Contains static analysis configuration flags, e.g., ThrowExceptionOnLog == true)*.
3. **`release.jks`** *(Currently mapped to live directly inside your `/app/` partition)*.
