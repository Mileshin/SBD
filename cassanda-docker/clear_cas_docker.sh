#!/bin/sh

#Stop nodes
docker stop some-cassandra3 some-cassandra some-cassandra2 coordinator-cassandra

#Delete nodes
docker rm some-cassandra some-cassandra2 some-cassandra3 coordinator-cassandra

