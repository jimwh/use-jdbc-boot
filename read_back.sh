#!/bin/bash

if [ "$1" = "" ]
then
  echo "Usage: $0 [dev|staging|prod]"
  exit
fi

java -Denv=$1 -Xms512M -Xmx1024M -XX:MaxPermSize=256M -jar ./target/jdbctester-1.0.jar
