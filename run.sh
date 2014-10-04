#!/bin/bash

#mvn clean install -DskipTests
mvn clean install -Pdebug-keystore,standard,generateDao,sign-debug android:deploy android:run
pushd plant_care_app
#mvn -Pdebug-keystore,sign-debug android:undeploy android:deploy android:run
popd
