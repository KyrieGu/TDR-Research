@echo off
title BCIFilter

javac -sourcepath ../../Components/BCIFilter -cp ../../Components/* ../../Components/BCIFilter/*.java
start "BCIFilter" /D"../../Components/BCIFilter" java -cp .;../* CreateBCIFilter