#!/bin/bash

set -e

/zeppelin/bin/zeppelin.sh &
ZEPPID=$!
sleep 20
curl -d@/mnistdemo-notebook.json http://127.0.0.1:8080/api/notebook/import
sleep 1
kill $ZEPPID
sleep 2
