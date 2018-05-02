#!/bin/sh
version="3.11.2"

# Create coordunator
#docker run --name coordinator-cassandra -p 9042:9042 -e MAX_HEAP_SIZE="256M" -e HEAP_NEWSIZE="64M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=coordinator -d cassandra:$version

#Create  nodes
docker run --name cassandra-node1 -p 9042:9042 -e MAX_HEAP_SIZE="500M" -e HEAP_NEWSIZE="256M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter1 -d cassandra:$version

docker run --name cassandra-node2 -p 9043:9042 -e MAX_HEAP_SIZE="500M" -e HEAP_NEWSIZE="256M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter2 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' cassandra-node1)" cassandra:$version

docker run --name cassandra-node3 -p 9044:9042 -e MAX_HEAP_SIZE="500M" -e HEAP_NEWSIZE="256M" -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter3 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' cassandra-node1)" cassandra:$version


