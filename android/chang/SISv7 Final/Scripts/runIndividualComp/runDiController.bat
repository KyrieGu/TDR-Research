@echo off
title DiController

javac -sourcepath ../../Components/DiController -cp ../../Components/* ../../Components/DiController/*.java
start "DiController" /D"../../Components/DiController" java -cp .;../* CreateDiController