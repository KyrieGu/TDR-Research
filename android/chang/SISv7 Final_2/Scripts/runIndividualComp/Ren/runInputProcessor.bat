@echo off
title InputProcessor

javac -sourcepath ../../Components/InputProcessor -cp ../../Components/* ../../Components/InputProcessor/*.java
start "InputProcessor" /D"../../Components/InputProcessor" java -cp .;../* -Djava.library.path="../" CreateInputProcessor