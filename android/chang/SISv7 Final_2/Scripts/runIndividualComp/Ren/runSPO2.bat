@echo off
title SPO2

javac -sourcepath ../../Component/SPO2 -cp ../../Components/* ../../Components/SPO2/*.java
start "SPO2" /D"../../Components/SPO2" java -cp .;../* CreateSPO2