@echo off
title FlowerGUI

javac -sourcepath ../../Components/FlowerGUI -cp ../../Components/* ../../Components/FlowerGUI/*.java
start "FlowerGUI" /D"../../Components/FlowerGUI" java -cp .;../* CreateFlowerGUI