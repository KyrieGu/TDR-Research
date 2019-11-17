#!/bin/bash
javac -sourcepath ../../Components/BCIUploader -cp '../../Components/*' ../../Components/BCIUploader/*.java

cd ../../Components/BCIUploader/
java CreateBCIUploader



