#!/bin/bash

BASE_DIR=$(dirname -- "$( readlink -f -- "$0"; )")

# chama o programa java DistriutedSystem
./build.sh

java -cp $BASE_DIR/bin/ Base
