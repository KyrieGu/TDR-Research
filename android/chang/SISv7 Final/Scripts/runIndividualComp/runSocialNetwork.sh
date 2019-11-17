#!/bin/bash
javac -sourcepath ../../Components/SocialNetwork -cp '../../Components/*' ../../Components/SocialNetwork/*.java

cd ../../Components/SocialNetwork/
java CreateSocialNetwork



