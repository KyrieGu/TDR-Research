@echo off
title RenGUI

javac -sourcepath ../../Components/RenGUI -cp ../../Components/* ../../Components/RenGUI/*.java
start "RenGUI" /D"../../Components/RenGUI" java -cp .;../* CreateRenGUI