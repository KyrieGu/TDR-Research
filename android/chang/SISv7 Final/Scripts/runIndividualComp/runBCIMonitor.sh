#!/bin/bash
javac -sourcepath ../../Components/BCIMonitor -cp '../../Components/*' ../../Components/BCIMonitor/*.java

cd ../../Components/BCIMonitor/
java CreateBCIMonitor



