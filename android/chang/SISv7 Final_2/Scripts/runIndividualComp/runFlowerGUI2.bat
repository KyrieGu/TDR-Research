@echo off
title FlowerGUI2

javac -sourcepath ../../Components/FlowerGUI2 -cp ../../Components/* ../../Components/FlowerGUI2/*.java
start "FlowerGUI2" /D"../../Components/FlowerGUI2" java -cp .;../* CreateFlowerGUI2