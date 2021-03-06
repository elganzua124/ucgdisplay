#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Do not deploy on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy (Reason: Pull-Request or Non-master branch detected)"
    exit 0
fi

if [[ $TRAVIS_OS_NAME == 'linux' ]] && [[ ${TRAVIS_PULL_REQUEST} == 'false' ]] && [[ ${TRAVIS_TAG} == '' ]]; then
    echo "  _____    ______   _____    _         ____   __     __"
    echo " |  __ \  |  ____| |  __ \  | |       / __ \  \ \   / /"
    echo " | |  | | | |__    | |__) | | |      | |  | |  \ \_/ / "
    echo " | |  | | |  __|   |  ___/  | |      | |  | |   \   /  "
    echo " | |__| | | |____  | |      | |____  | |__| |    | |   "
    echo " |_____/  |______| |_|      |______|  \____/     |_|   "
    echo "                                                       "

    # Deploy to SONATYPE repository
    mvn deploy -P'deploy-sonatype' -Dcompile.native=true -Dmaven.antrun.skip=true -DskipTests -Dlicense.skipUpdateLicense=true --settings scripts/settings.xml

    # Deploy to GITHUB repository
    mvn deploy -P'deploy-github' -Dcompile.native=true -Dmaven.antrun.skip=true -DskipTests -Dlicense.skipUpdateLicense=true --settings scripts/settings.xml
fi