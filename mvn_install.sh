#!/bin/bash

#
#   mvn install
#   create by ztf
#

echo "start auto mvn install"

cd JRPlat_Core
mvn clean
mvn install
cd ..
cd JRPlat_Module
mvn clean
mvn install
cd ..
cd JRPlat_Web
mvn clean
mvn install

echo "mvn install over.."