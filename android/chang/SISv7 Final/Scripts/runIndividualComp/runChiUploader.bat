@echo off
title ChiUploader

javac -sourcepath ../../Components/ChiUploader -cp ../../Components/* ../../Components/ChiUploader/*.java
start "ChiUploader" /D"../../Components/ChiUploader" java -cp .;../* CreateChiUploader