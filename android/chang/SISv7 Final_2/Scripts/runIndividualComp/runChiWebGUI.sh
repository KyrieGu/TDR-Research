#!/bin/bash
javac -sourcepath ../../Components/ChiWebGUI -cp '../../Components/*' ../../Components/ChiWebGUI/*.java

cd ../../Components/ChiWebGUI/
java CreateChiWebGUI


