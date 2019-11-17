#!/bin/bash
javac -sourcepath ../../Components/InputProcessor -cp '../../Components/*' ../../Components/InputProcessor/*.java

cd ../../Components/InputProcessor/
java -cp :../* CreateInputProcessor


