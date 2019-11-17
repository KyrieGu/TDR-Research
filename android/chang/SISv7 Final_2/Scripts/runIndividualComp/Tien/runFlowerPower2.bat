@echo off
title FlowerPower2

javac -sourcepath ../../Component/FlowerPower2 -cp ../../Components/* ../../Components/FlowerPower2/*.java
start "FlowerPower2" /D"../../Components/FlowerPower2" java -cp .;../* CreateFlowerPower2