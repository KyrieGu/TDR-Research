@echo off
title ChiMonitor

javac -sourcepath ../../Components/ChiMonitor -cp ../../Components/* ../../Components/ChiMonitor/*.java
start "ChiMonitor" /D"../../Components/ChiMonitor" java -cp .;../* CreateChiMonitor