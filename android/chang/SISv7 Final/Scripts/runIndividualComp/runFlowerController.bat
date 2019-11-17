@echo off
title FlowerController

javac -sourcepath ../../Components/FlowerController -cp ../../Components/* ../../Components/FlowerController/*.java
start "FlowerController" /D"../../Components/FlowerController" java -cp .;../* CreateFlowerController