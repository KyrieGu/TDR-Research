#!/bin/bash
javac -sourcepath ../../Components/ChiMonitor -cp '../../Components/*' ../../Components/ChiMonitor/*.java

cd ../../Components/ChiMonitor/
java -cp :../* CreateChiMonitor


