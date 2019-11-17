@echo off
title FlowerUploader

javac -sourcepath ../../Components/FlowerUploader -cp ../../Components/* ../../Components/FlowerUploader/*.java
start "FlowerUploader" /D"../../Components/FlowerUploader" java -cp .;../* CreateFlowerUploader