@echo off
title FlowerManager

javac -sourcepath ../../Components/FlowerManager -cp ../../Components/* ../../Components/FlowerManager/*.java
start "FlowerManager" /D"../../Components/FlowerManager" java -cp .;../* CreateFlowerManager