@echo off
title FlowerPower

javac -sourcepath ../../Component/FlowerPower -cp ../../Components/* ../../Components/FlowerPower/*.java
start "FlowerPower" /D"../../Components/FlowerPower" java -cp .;../* CreateFlowerPower