#!/usr/bin/env bash
set -e

echo '  _____ _   _  _____ _______       _      _      '
echo ' |_   _| \ | |/ ____|__   __|/\   | |    | |     '
echo '   | | |  \| | (___    | |  /  \  | |    | |     '
echo '   | | | . ` |\___ \   | | / /\ \ | |    | |     '
echo '  _| |_| |\  |____) |  | |/ ____ \| |____| |____ '
echo ' |_____|_| \_|_____/   |_/_/    \_\______|______|'
echo '                                                 '

shopt -s nocasematch
case "$1" in
  Debug|Release|RelWithDebInfo|MinSizeRel)
    BUILD_TYPE=$1
    ;;
  *)
    echo "Unrecognized build type argument: ${1}. Defaulting to 'Debug' type"
    BUILD_TYPE=Debug
    ;;
esac
shopt -u nocasematch

if [[ ${TRAVIS_OS_NAME} == 'linux' || ($(uname -o) == 'GNU/Linux' && $(uname -m) == 'x86_64') ]]; then
    CROSS_COMPILE=yes
else
    CROSS_COMPILE=no
fi

MVN_PROPS="-DbuildType=${BUILD_TYPE} -Dlicense.skipUpdateLicense=true -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V"

echo "[INSTALL] Build Type = ${BUILD_TYPE}, Cross Compile = ${CROSS_COMPILE}, Properties = ${MVN_PROPS}"

# Only cross-compile on linux (x86_64/amd64) environment
if [[ ${CROSS_COMPILE} == 'yes' ]]; then
    mvn --settings scripts/settings.xml install -P'cross-compile' ${MVN_PROPS}
else
    mvn --settings scripts/settings.xml install ${MVN_PROPS}
fi