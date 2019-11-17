@echo off
title EKG

javac -sourcepath ../../Component/EKG -cp ../../Components/* ../../Components/EKG/*.java
start "EKG" /D"../../Components/EKG" java -cp .;../* CreateEKG