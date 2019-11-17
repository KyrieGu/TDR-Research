@echo off
title SocialNetwork

javac -sourcepath ../../Components/SocialNetwork -cp ../../Components/* ../../Components/SocialNetwork/*.java
start "SocialNetwork" /D"../../Components/SocialNetwork" java -cp .;../* CreateSocialNetwork
