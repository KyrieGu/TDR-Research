@echo off
title SocialNetworkSensor

javac -sourcepath ../../Components/SocialNetworkSensor -cp ../../Components/* ../../Components/SocialNetworkSensor/*.java
start "SocialNetworkSensor" /D"../../Components/SocialNetworkSensor" java -cp .;../* CreateSocialNetworkSensor