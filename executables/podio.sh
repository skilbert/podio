#!/bin/sh
# launcher.sh
# navigate to home directory, then to this directory, then execute podio.jar, then back home

cd /
cd home/pi/Podio
sudo java -jar Podio.jar
cd /
