#!/bin/bash

set -e

/zeppelin/bin/zeppelin.sh &
ZEPPID=$!
sleep 10
curl -d@/mnistdemo-notebook.json http://localhost:8080/api/notebook/import
sleep 1
kill $ZEPPID
sleep 2
