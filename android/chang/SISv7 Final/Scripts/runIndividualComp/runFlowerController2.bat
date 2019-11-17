@echo off
title FlowerController2

javac -sourcepath ../../Components/FlowerController2 -cp ../../Components/* ../../Components/FlowerController2/*.java
start "FlowerController2" /D"../../Components/FlowerController2" java -cp .;../* CreateFlowerController2