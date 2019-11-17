@echo off
title BCIUploader

javac -sourcepath ../../Components/BCIUploader -cp ../../Components/* ../../Components/BCIUploader/*.java
start "BCIUploader" /D"../../Components/BCIUploader" java -cp .;../* CreateBCIUploader