#!/bin/bash
set +x
logFile='launcher.log'
touch $logFile
echo "Launcher started \n" &>> $logFile
which java &>> $logFile
java -version &>> $logFile
java -jar learnOpenGL.jar &>> $logFile