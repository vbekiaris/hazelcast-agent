#!/usr/bin/env bash
# usage start.sh VERSION CONFIG_URL WORKING_DIR
# HAZELCAST_OSS_URL="http://download.hazelcast.com/download.jsp?version=hazelcast-$1&p="
HAZELCAST_OSS_URL="http://192.168.2.29:8083/mancenter/stylesheets/hazelcast-$1.zip"
HAZELCAST_OSS_DIRECTORY="hazelcast-$1"
HAZELCAST_OSS_FILENAME="${HAZELCAST_OSS_DIRECTORY}.zip"
pushd $3
wget -q -O ${HAZELCAST_OSS_FILENAME} ${HAZELCAST_OSS_URL}
# tar xzf ${HAZELCAST_OSS_FILENAME}
unzip -qq -o ${HAZELCAST_OSS_FILENAME}
pushd ${HAZELCAST_OSS_DIRECTORY}
mv bin/hazelcast.xml bin/hazelcast.xml.original
wget -O bin/hazelcast.xml $2
pushd bin
/bin/bash `pwd`/start.sh > `pwd`/nohup.out 2>&1
popd
popd
popd