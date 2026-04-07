# Project Initialization Guide

Welcome to the project! This repository contains a local initialization script designed to immediately get your environment ready for development securely and effectively.

## What is `init_project.sh`?

When a developer clones this repository for the very first time, there are a few manual configuration hurdles they usually have to cross (like setting up global permissions or configuring security secrets). 

`init_project.sh` is an automated initialization script. Running it will:
1. Grant your machine execution permissions (`chmod +x`) over all internal bash tools (`build_apk.sh`, `new_feature.sh`).
2. Install strict **Pre-Commit Git Hooks** that enforce our static analysis requirements via Detekt, meaning bad code or formatting gets rejected locally before committing.
3. Automatically generate an empty `keystore.properties` template file so Android Studio doesn't crash searching for security profiles when performing Release configuration.

## How to use

Ensure you are located inside the root project directory, then run the initialization script via your terminal:

```bash
chmod +x init_project.sh
./init_project.sh
```

**Note:** If you see a warning stating `gradlew not found`, simply open your repository inside **Android Studio**. Android Studio's automated *Gradle Sync* will natively build the Gradle Wrappers (`gradlew`) for your machine. Once the sync finishes, you are completely setup!
