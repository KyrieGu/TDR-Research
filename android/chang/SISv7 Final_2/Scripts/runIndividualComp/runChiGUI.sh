#!/bin/bash
javac -sourcepath ../../Components/ChiGUI -cp '../../Components/*' ../../Components/ChiGUI/*.java

cd ../../Components/ChiGUI/
java CreateChiGUI


