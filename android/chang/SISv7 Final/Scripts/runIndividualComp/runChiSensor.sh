#!/bin/bash
javac -sourcepath ../../Components/ChiSensor -cp '../../Components/*' ../../Components/ChiSensor/*.java

cd ../../Components/ChiSensor/
java CreateChiSensor

