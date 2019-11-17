@echo off
title ReadingBehavior

javac -sourcepath ../../Component/ReadingBehavior -cp ../../Components/* ../../Components/ReadingBehavior/*.java
start "ReadingBehavior" /D"../../Components/ReadingBehavior" java -cp .;../* CreateReadingBehavior