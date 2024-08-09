#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Usage: ./spellcheck.sh (<pathToDictFile>|langtool) <pathToTestFile>"
    exit 1
fi

dictFilePath="$1"
testFilePath="$2"

targetJar="target/spellchecker-0.0.1-SNAPSHOT.jar"

if [ ! -f "$targetJar" ]; then
    ./mvnw package 1>/dev/null 2>&1
fi
java -jar $targetJar $dictFilePath $testFilePath
