@echo off
title RenController

javac -sourcepath ../../Components/RenController -cp ../../Components/* ../../Components/RenController/*.java
start "RenController" /D"../../Components/RenController" java -cp .;../* CreateRenController