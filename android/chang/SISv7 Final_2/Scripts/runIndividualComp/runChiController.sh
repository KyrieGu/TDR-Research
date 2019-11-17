#!/bin/bash
javac -sourcepath ../../Components/ChiController -cp '../../Components/*' ../../Components/ChiController/*.java

cd ../../Components/ChiController/
java CreateChiController
