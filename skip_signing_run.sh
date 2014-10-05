#!/bin/bash

cd application  && mvn -e clean install -Pstaging android:deploy android:run -DskipTests
