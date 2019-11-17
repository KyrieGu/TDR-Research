@echo off
title KinectMonitor

javac -sourcepath ../../Components/KinectMonitor -cp ../../Components/* ../../Components/KinectMonitor/*.java
start "KinectMonitor" /D"../../Components/KinectMonitor" java -cp .;../* CreateKinectMonitor