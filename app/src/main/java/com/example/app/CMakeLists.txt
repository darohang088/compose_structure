# Check out the source.
git clone https://gitlab.com/bzip/bzip2.git
cd bzip2

# Only choose one of these, depending on your build machine...
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/darwin-x86_64
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/linux-x86_64

# Only choose one of these, depending on your device...
export TARGET=aarch64-linux-android
export TARGET=armv7a-linux-androideabi
export TARGET=i686-linux-android
export TARGET=x86_64-linux-android

# Set this to your minSdkVersion.
export API=21

# Build.
make \
CC="$TOOLCHAIN/bin/clang --target=$TARGET$API" \
AR=$TOOLCHAIN/bin/llvm-ar \
RANLIB=$TOOLCHAIN/bin/llvm-ranlib \
bzip2