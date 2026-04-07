# Android Build Environments Guide

This project is configured with a robust flavor/build-type dimensional matrix supporting completely isolated environments.

## Available Flavors
- **dev**: Points to `dev-api.example.com`, enables logging, appended with `.dev` package name.
- **staging**: Points to `staging-api.example.com`, enables logging, appended with `.staging` package name.
- **preprod**: Points to `preprod-api.example.com`, logging disabled, appended with `.preprod` package name.
- **production**: Points to `api.example.com`, logging disabled, standard package name.

## Release Building Prerequisites
Before building a `release` variant (which applies Minification/R8), you must configure your Keystore settings.
1. Open the generated `keystore.properties` at the root of the project.
2. Edit the placeholder values and ensure `storeFile` points to your actual `.jks` file location.
> **Warning**: Never commit your real keystore or passwords to public version control! `.gitignore` handles `keystore.properties` natively.

## Automated Builder Script
We've provided a bash script to rapidly generate and export any variant. It packages the raw gradle output into a readable, timestamped `.apk` placed in the `builds/` directory natively.

Before using, ensure the script is executable:
```bash
chmod +x build_apk.sh
```

### Usage
```bash
./build_apk.sh <flavor> <build_type>
```

#### Full Argument Combinations
| Command | Output Use-Case |
| :--- | :--- |
| `./build_apk.sh dev debug` | Quick daily development |
| `./build_apk.sh dev release` | Validating minification logic on DEV |
| `./build_apk.sh staging debug` | Internal QA testing un-minified |
| `./build_apk.sh staging release` | Client QA testing |
| `./build_apk.sh preprod debug` | Dry run production settings with debugging |
| `./build_apk.sh preprod release` | Final client signoff build |
| `./build_apk.sh production debug` | Edge-case diagnostics |
| `./build_apk.sh production release` | Play Store upload artifact |

## Installing the Exported Artifact
Once generated, you can find the output APK in the generated `/builds` directory. Connect an emulator or physical device via ADB and install directly:
```bash
adb install builds/MyApp-Staging-Release-v1.0-STAGING-20240214.apk
```
