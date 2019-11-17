@echo off
title DiGUI

javac -sourcepath ../../Components/DiGUI -cp ../../Components/* ../../Components/DiGUI/*.java
start "DiGUI" /D"../../Components/DiGUI" java -cp .;../* CreateDiGUI