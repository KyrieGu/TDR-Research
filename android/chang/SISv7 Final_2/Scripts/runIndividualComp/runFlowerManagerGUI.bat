@echo off
title FlowerManagerGUI

javac -sourcepath ../../Components/FlowerManagerGUI -cp ../../Components/* ../../Components/FlowerManagerGUI/*.java
start "FlowerManagerGUI" /D"../../Components/FlowerManagerGUI" java -cp .;../* CreateFlowerManagerGUI