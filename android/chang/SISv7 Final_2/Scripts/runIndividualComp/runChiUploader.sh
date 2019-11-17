#!/bin/bash
javac -sourcepath ../../Components/ChiUploader -cp '../../Components/*' ../../Components/ChiUploader/*.java

cd ../../Components/ChiUploader/
java CreateChiUploader



