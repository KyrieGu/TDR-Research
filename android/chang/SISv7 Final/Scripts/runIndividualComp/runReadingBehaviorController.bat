@echo off
title ReadingBehaviorController

javac -sourcepath ../../Components/ReadingBehaviorController -cp ../../Components/* ../../Components/ReadingBehaviorController/*.java
start "ReadingBehaviorController" /D"../../Components/ReadingBehaviorController" java -cp .;../* CreateReadingBehaviorController