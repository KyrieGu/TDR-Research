@echo off
title ChiSensor

javac -sourcepath ../../Components/ChiSensor -cp ../../Components/* ../../Components/ChiSensor/*.java
start "ChiSensor" /D"../../Components/ChiSensor" java -cp .;../* CreateChiSensor