@echo off
title ChiGUI

javac -sourcepath ../../Components/ChiGUI -cp ../../Components/* ../../Components/ChiGUI/*.java
start "ChiGUI" /D"../../Components/ChiGUI" java -cp .;../* CreateChiGUI
