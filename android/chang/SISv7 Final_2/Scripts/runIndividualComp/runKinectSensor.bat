@echo off
title KinectSensor

javac -sourcepath ../../Components/KinectSensor -cp ../../Components/* ../../Components/KinectSensor/*.java
start "KinectSensor" /D"../../Components/KinectSensor" java -cp .;../* CreateKinectSensor