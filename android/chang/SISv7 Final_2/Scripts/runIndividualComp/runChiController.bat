@echo off
title ChiController

javac -sourcepath ../../Components/ChiController -cp ../../Components/* ../../Components/ChiController/*.java
start "ChiController" /D"../../Components/ChiController" java -cp .;../* CreateController
