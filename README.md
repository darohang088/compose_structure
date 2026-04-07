# Android Jetpack Compose Architecture Template

A complete Android scaffold architecture ready for production. Built with modern Android development standards.

## Features & Tech Stack

- **UI:** Jetpack Compose (Material 3, standard theming, dynamic colors, dark mode support)
- **Architecture:** MVVM + Clean Architecture principles (Offline-first approach)
- **Dependency Injection:** Dagger Hilt
- **Network Interface:** Retrofit 2 + OkHttp Logging Interceptor
- **Local Cache:** Room Database
- **Key-Value Storage:** Preferences DataStore
- **Concurrency:** Kotlin Coroutines & Flow
- **Image Loading:** Coil (Configured for future use via `libs.versions.toml`)
- **Testing Setup:**
  - JUnit 4
  - MockK
  - Turbine (For Flow testing)
  - Compose Test Rule
- **CI/CD:** GitHub Actions configured to build and test every push/PR to `main`.
- **Other utilities:**
  - SplashScreen API integration (`androidx.core:core-splashscreen`).
  - Timber for advanced debug logging.

## Setup Instructions

### Pre-requisites
- Android Studio Iguana / Jellyfish (or latest).
- Ensure JDK 17 is configured in project settings.

### Available Environments (Flavors)
This project is configured with a robust flavor/build-type dimensional matrix supporting completely isolated environments. The `BASE_URL` and `ENVIRONMENT_NAME` are dynamically injected automatically.

- **dev**: Points to `https://dev-api.example.com/`, enables logging, package suffix `.dev`
- **staging**: Points to `https://staging-api.example.com/`, enables logging, package suffix `.staging`
- **preprod**: Points to `https://preprod-api.example.com/`, logging disabled, package suffix `.preprod`
- **production**: Points to `https://api.example.com/`, logging disabled, standard package name

*(Note: Prior to building `release` configurations, ensure you fill out your actual keystore credentials inside `keystore.properties` at the root of the project.)*

### Automating the APK Build Flow

We have provided a comprehensive automated build script `build_apk.sh` that perfectly generates and documents the entire end-to-end execution flow of building APKs for EVERY environment.

First, ensure the script is executable:
```bash
chmod +x build_apk.sh
```

**Generate your environment variant seamlessly:**
Run the script using the syntax `./build_apk.sh <flavor> <build_type>` using any of the available 8 combinations:

| Build Command | Output Use-Case |
| :--- | :--- |
| `./build_apk.sh dev debug` | Quick daily development connecting to DEV APIs |
| `./build_apk.sh dev release` | Validating minification logic on DEV |
| `./build_apk.sh staging debug` | Internal QA testing un-minified |
| `./build_apk.sh staging release` | Client QA testing identical to production |
| `./build_apk.sh preprod debug` | Dry run production settings with debugging |
| `./build_apk.sh preprod release` | Final client signoff build |
| `./build_apk.sh production debug` | Edge-case diagnostics |
| `./build_apk.sh production release` | Final Play Store upload artifact |

**Output Location:**
The script intercepts the APK from deep within Gradle's output paths and cleanly extracts it to an easily accessible `builds/` directory in the root of the project, tagged with accurate naming conventions and timestamps:
`builds/MyApp-Staging-Release-v1.0-STAGING-20240214.apk`

From here, you can seamlessly manually distribute or ADB install!
## Known Placeholders
- The `ExampleModel`, `ExampleDao`, and `ExampleScreen` are built around an arbitrary API list response. If your chosen API dictates nested objects, ensure you update these classes appropriately!
