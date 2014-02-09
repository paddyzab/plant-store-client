#!/bin/bash

#mvn clean install -DskipTests
mvn clean install
pushd plant_care_app
mvn -Pdebug-keystore,sign-debug android:deploy android:run
popd
