#!/bin/sh
version="3.11.2"

# Create coordunator
docker run --name coordinator-cassandra -p 9042:9042 -e MAX_HEAP_SIZE="256M" -e HEAP_NEWSIZE="64M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=coordinator -d cassandra:$version

#Create  nodes
docker run --name some-cassandra -e MAX_HEAP_SIZE="500M" -e HEAP_NEWSIZE="256M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter1 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' coordinator-cassandra)" cassandra:$version
docker run --name some-cassandra2 -e MAX_HEAP_SIZE="500M" -e HEAP_NEWSIZE="256M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter2 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' coordinator-cassandra)" cassandra:$version
docker run --name some-cassandra3 -e MAX_HEAP_SIZE="500M" -e HEAP_NEWSIZE="256M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter3 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' coordinator-cassandra)" cassandra:$version


