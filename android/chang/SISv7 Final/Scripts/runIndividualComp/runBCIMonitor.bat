@echo off
title BCIMonitor

javac -sourcepath ../../Components/BCIMonitor -cp ../../Components/* ../../Components/BCIMonitor/*.java
start "BCIMonitor" /D"../../Components/BCIMonitor" java -cp .;../* CreateBCIMonitor