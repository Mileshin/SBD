#!/bin/sh
store_path=/var/lib/cassandra

#Stop nodes
docker stop some-cassandra3 
docker stop some-cassandra2 
docker stop some-cassandra 

#Delete nodes
docker rm some-cassandra 
docker rm some-cassandra2 
docker rm some-cassandra3 

# Delete storage
rm -rf $store_path/c1
rm -rf $store_path/c2
rm -rf $store_path/c3
