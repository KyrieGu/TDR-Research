@echo off
title FlowerUploader2

javac -sourcepath ../../Components/FlowerUploader2 -cp ../../Components/* ../../Components/FlowerUploader2/*.java
start "FlowerUploader2" /D"../../Components/FlowerUploader2" java -cp .;../* CreateFlowerUploader2