#!/bin/bash
javac -sourcepath ../../Components/SocialNetworkSensor -cp '../../Components/*' ../../Components/SocialNetworkSensor/*.java

cd ../../Components/SocialNetworkSensor/
java SocialNetworkChiSensor

