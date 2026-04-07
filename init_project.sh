#!/usr/bin/env bash

# ANSI color codes
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${CYAN}=================================================${NC}"
echo -e "${CYAN}▶ Initializing Project Workspace...${NC}"
echo -e "${CYAN}=================================================${NC}"

# 1. Provide Executable Permissions Contextually
echo "1. Configuring permissions for shell scripts..."
chmod +x gradlew || true
chmod +x build_apk.sh || true
chmod +x new_feature.sh || true
chmod +x init_project.sh || true
echo -e "${GREEN}✓ Scripts are now executable.${NC}"

# 2. Setup Git Hooks
echo "2. Installing Git Pre-commit Hooks..."
if [ -d ".git" ]; then
    mkdir -p .git/hooks
    cp .githooks/pre-commit .git/hooks/pre-commit
    chmod +x .git/hooks/pre-commit
    echo -e "${GREEN}✓ Strict Git rules (detekt/logging) are installed locally.${NC}"
else
    echo -e "${YELLOW}⚠ Warning: No .git directory found. Initialize a git repository ('git init') first and re-run to secure hooks.${NC}"
fi

# 3. Handle Local Properties (Keystore checks)
echo "3. Checking Keystore Properties..."
if [ ! -f "keystore.properties" ]; then
    echo -e "${YELLOW}⚠ Warning: keystore.properties is missing.${NC}"
    echo "Creating a placeholder keystore.properties..."
    cat <<EOF > keystore.properties
# Replace these placeholder values with your actual release keystore credentials
storePassword=my_store_password
keyPassword=my_key_password
keyAlias=my_key_alias
storeFile=release.jks
EOF
    echo -e "${GREEN}✓ Placeholder keystore.properties created.${NC}"
else
    echo -e "${GREEN}✓ keystore.properties exists.${NC}"
fi

# 4. Perform an initial debug build
echo "4. Executing Initial Clean/Build to verify dependencies..."
if [ -f "gradlew" ]; then
    if ./gradlew clean assembleDebug --stacktrace; then
        echo -e "${GREEN}=================================================${NC}"
        echo -e "${GREEN}🎉 Project initialization complete! You're ready to code.${NC}"
        echo -e "${GREEN}=================================================${NC}"
    else
        echo ""
        echo -e "${RED}=================================================${NC}"
        echo -e "${RED}❌ Initial build failed! Review the stacktrace above!${NC}"
        echo -e "${RED}=================================================${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}ℹ 'gradlew' not found yet. Please open this project inside Android Studio and allow Gradle Sync to generate it automatically.${NC}"
    echo -e "${GREEN}🎉 Pre-requisites initialized successfully!${NC}"
fi
