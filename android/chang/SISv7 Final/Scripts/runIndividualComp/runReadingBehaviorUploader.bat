@echo off
title ReadingBehaviorUploader

javac -sourcepath ../../Components/ReadingBehaviorUploader -cp ../../Components/* ../../Components/ReadingBehaviorUploader/*.java
start "ReadingBehaviorUploader" /D"../../Components/ReadingBehaviorUploader" java -cp .;../* CreateReadingBehaviorUploader