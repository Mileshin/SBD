#!/bin/sh
version="3.11.2"
storage_path=/var/lib/cassandra
# Create storage
mkdir $storage_path/c1
mkdir $storage_path/c2
mkdir $storage_path/c3

#Create first node
docker run --name some-cassandra -p 9042:9042 -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter1 -d cassandra:$version
docker run --name some-cassandra2 -p 9043:9042 -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter2 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' some-cassandra)" cassandra:$version
docker run --name some-cassandra3 -p 9044:9042 -e CASSANDRA_CLUSTER_NAME="wiki" -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -e CASSANDRA_DC=datacenter3 -d -e CASSANDRA_SEEDS="$(docker inspect --format='{{ .NetworkSettings.IPAddress }}' some-cassandra)" cassandra:$version