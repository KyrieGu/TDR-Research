@echo off
title WebGUI

javac -sourcepath ../../Components/ChiWebGUI -cp ../../Components/* ../../Components/ChiWebGUI/*.java
start "ChiWebGUI" /D"../../Components/ChiWebGUI" java -cp .;../* CreateChiWebGUI