@echo off
title ReadingBehaviorManager

javac -sourcepath ../../Components/ReadingBehaviorManager -cp ../../Components/* ../../Components/ReadingBehaviorManager/*.java
start "ReadingBehaviorManager" /D"../../Components/ReadingBehaviorManager" java -cp .;../* CreateReadingBehaviorManager