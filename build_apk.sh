#!/usr/bin/env bash

# This script builds Android APKs based on flavor and build type.
set -e

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

show_help() {
    echo -e "${CYAN}Usage: ./build_apk.sh <flavor> <build_type>${NC}"
    echo ""
    echo "Available Flavors:    dev, staging, preprod, production"
    echo "Available Build Types: debug, release"
    echo ""
    echo -e "${YELLOW}Examples:${NC}"
    echo "  ./build_apk.sh dev debug"
    echo "  ./build_apk.sh dev release"
    echo "  ./build_apk.sh staging debug"
    echo "  ./build_apk.sh staging release"
    echo "  ./build_apk.sh preprod debug"
    echo "  ./build_apk.sh preprod release"
    echo "  ./build_apk.sh production debug"
    echo "  ./build_apk.sh production release"
}

if [ "$#" -ne 2 ]; then
    echo -e "${RED}Error: Missing arguments.${NC}"
    show_help
    exit 1
fi

if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    show_help
    exit 0
fi

FLAVOR=$(echo "$1" | tr '[:upper:]' '[:lower:]')
BUILD_TYPE=$(echo "$2" | tr '[:upper:]' '[:lower:]')

# Validate Flavor
case "$FLAVOR" in
    dev|staging|preprod|production) ;;
    *) 
       echo -e "${RED}Error: Invalid flavor '$FLAVOR'. Must be dev, staging, preprod, or production.${NC}"
       exit 1 ;;
esac

# Validate Build Type
case "$BUILD_TYPE" in
    debug|release) ;;
    *) 
       echo -e "${RED}Error: Invalid build type '$BUILD_TYPE'. Must be debug or release.${NC}"
       exit 1 ;;
esac

# Convert to PascalCase for Gradle Tasks
PASCAL_FLAVOR=$(echo "$FLAVOR" | awk '{print toupper(substr($0,1,1)) substr($0,2)}')
PASCAL_BUILD_TYPE=$(echo "$BUILD_TYPE" | awk '{print toupper(substr($0,1,1)) substr($0,2)}')

GRADLE_TASK="assemble${PASCAL_FLAVOR}${PASCAL_BUILD_TYPE}"
UPLOAD_TASK="appDistributionUpload${PASCAL_FLAVOR}${PASCAL_BUILD_TYPE}"

# Determine Base URL for display
case "$FLAVOR" in
    dev) BASE_URL="https://dev-api.example.com/" ;;
    staging) BASE_URL="https://staging-api.example.com/" ;;
    preprod) BASE_URL="https://preprod-api.example.com/" ;;
    production) BASE_URL="https://api.example.com/" ;;
esac

# Set up constants for output
APP_NAME="MyApp"
# Get the version name from build.gradle.kts (simple extraction, defaults to 0.1)
VERSION_NAME="0.1"

OUTPUT_FILENAME="V-${FLAVOR}-${BUILD_TYPE}-${VERSION_NAME}.apk"
DEST_DIR="${PWD}/builds"
DEST_FILE="${DEST_DIR}/${OUTPUT_FILENAME}"

mkdir -p "$DEST_DIR"

echo "================================================="
echo -e "${CYAN}▶ Android Environment Builder${NC}"
echo "================================================="
echo -e "Flavor     : ${YELLOW}${FLAVOR}${NC}"
echo -e "Build Type : ${YELLOW}${BUILD_TYPE}${NC}"
echo -e "Target URL : ${CYAN}${BASE_URL}${NC}"
echo -e "Task       : ${YELLOW}./gradlew ${GRADLE_TASK}${NC}"
echo -e "Output to  : ${GREEN}builds/${OUTPUT_FILENAME}${NC}"
echo "================================================="
echo "Building APK... This may take a few minutes."
echo ""

# Run Gradle Build
if ./gradlew "${GRADLE_TASK}" --stacktrace; then
    echo ""
    echo -e "${GREEN}✓ Build succeeded! Copying APK...${NC}"
    
    # Locate output APK. Gradle path formats generally look like this:
    # app/build/outputs/apk/dev/debug/app-dev-debug.apk
    # app/build/outputs/apk/staging/release/app-staging-release.apk
    
    SRC_APK_DIR="app/build/outputs/apk/${FLAVOR}/${BUILD_TYPE}"
    
    if [ -d "$SRC_APK_DIR" ]; then
        APK_FILE=$(find "$SRC_APK_DIR" -name "*.apk" | head -n 1)
        if [ -n "$APK_FILE" ]; then
            cp "$APK_FILE" "$DEST_FILE"
            FILE_SIZE=$(du -h "$DEST_FILE" | cut -f1)
            echo -e "${GREEN}=================================================${NC}"
            echo -e "${GREEN}🎉 Success!${NC}"
            echo -e "${CYAN}Output File : ${DEST_FILE}${NC}"
            echo -e "${CYAN}File Size   : ${FILE_SIZE}${NC}"
            echo -e "${GREEN}=================================================${NC}"
            
            echo -e "\n${CYAN}▶ Triggering Firebase App Distribution Upload...${NC}"
            if ./gradlew "${UPLOAD_TASK}" --stacktrace; then
                echo -e "${GREEN}🎉 Firebase Upload Successful!${NC}"
            else
                echo -e "${YELLOW}⚠️ APK Built successfully, but Firebase Upload failed! Check your authentication.${NC}"
            fi
            
        else
            echo -e "${RED}Error: Build succeeded but no APK found in ${SRC_APK_DIR}${NC}"
            exit 1
        fi
    else
        echo -e "${RED}Error: Source directory ${SRC_APK_DIR} does not exist. Check Gradle configuration.${NC}"
        exit 1
    fi
else
    echo ""
    echo -e "${RED}=================================================${NC}"
    echo -e "${RED}❌ Build Failed! Check the stacktrace above.${NC}"
    echo -e "${RED}=================================================${NC}"
    exit 1
fi
