@echo off
title ReadingBehaviorGUI

javac -sourcepath ../../Components/ReadingBehaviorGUI -cp ../../Components/* ../../Components/ReadingBehaviorGUI/*.java
start "ReadingBehaviorGUI" /D"../../Components/ReadingBehaviorGUI" java -cp .;../* CreateReadingBehaviorGUI