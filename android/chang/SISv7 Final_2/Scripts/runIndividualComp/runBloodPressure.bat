@echo off
title BloodPressure

javac -sourcepath ../../Component/BloodPressure -cp ../../Components/* ../../Components/BloodPressure/*.java
start "BloodPressure" /D"../../Components/BloodPressure" java -cp .;../* CreateBloodPressure