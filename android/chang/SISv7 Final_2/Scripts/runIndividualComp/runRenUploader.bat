@echo off
title RenUploader

javac -sourcepath ../../Components/RenUploader -cp ../../Components/* ../../Components/RenUploader/*.java
start "RenUploader" /D"../../Components/RenUploader" java -cp .;../* CreateRenUploader