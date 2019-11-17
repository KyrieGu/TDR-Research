@echo off
title DiUploader

javac -sourcepath ../../Components/DiUploader -cp ../../Components/* ../../Components/DiUploader/*.java
start "DiUploader" /D"../../Components/DiUploader" java -cp .;../* CreateDiUploader