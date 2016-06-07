#!/bin/sh
# launcher.sh
# navigate to home directory, then to this directory, then execute podio.jar, then back home

sleep 10
cd /
cd home/pi/Downloads/podioexc/
sudo java -jar -Djava.library.path=/usr/lib/jni  Podio.jar
cd /
