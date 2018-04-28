mkdir plugins
cd plugins
wget https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/3.3.0.2/apoc-3.3.0.2-all.jar
cd ..


docker network create --driver=bridge neo4j_cluster

docker run --name=neo4j_node1 --detach -p 7474:7474 -p 7687:7687 --net=neo4j_cluster --hostname=neo4j_node1 \
    --volume=$PWD/neo4j/logs1:/logs \
    --env=NEO4J_dbms_mode=HA --env=NEO4J_ha_serverId=1 \
    --env=NEO4J_ha_host_coordination=neo4j_node1:5001 --env=NEO4J_ha_host_data=neo4j_node1:6001 \
    --env=NEO4J_ha_initial__hosts=neo4j_node1:5001,neo4j_node2:5001,neo4j_node3:5001 \
    --env=NEO4J_ACCEPT_LICENSE_AGREEMENT=yes \
    --env=NEO4J_dbms_memory_pagecache_size=1G \
    -v $PWD/plugins:/plugins \
    neo4j:3.3.0-enterprise

docker run --name=neo4j_node2 --detach -p 7475:7474 -p 7688:7687 --net=neo4j_cluster --hostname=neo4j_node2 \
    --volume=$PWD/neo4j/logs2:/logs \
    --env=NEO4J_dbms_mode=HA --env=NEO4J_ha_serverId=2 \
    --env=NEO4J_ha_host_coordination=neo4j_node2:5001 --env=NEO4J_ha_host_data=neo4j_node2:6001 \
    --env=NEO4J_ha_initial__hosts=neo4j_node1:5001,neo4j_node2:5001,neo4j_node3:5001 \
    --env=NEO4J_ACCEPT_LICENSE_AGREEMENT=yes \
    --env=NEO4J_dbms_memory_pagecache_size=1G \
    -v $PWD/plugins:/plugins \
    neo4j:3.3.0-enterprise

docker run --name=neo4j_node3 --detach -p 7476:7474 -p 7689:7687 --net=neo4j_cluster --hostname=neo4j_node3 \
    --volume=$PWD/neo4j/logs3:/logs \
    --env=NEO4J_dbms_mode=HA --env=NEO4J_ha_serverId=3 \
    --env=NEO4J_ha_host_coordination=neo4j_node3:5001 --env=NEO4J_ha_host_data=neo4j_node3:6001 \
    --env=NEO4J_ha_initial__hosts=neo4j_node1:5001,neo4j_node2:5001,neo4j_node3:5001 \
    --env=NEO4J_ACCEPT_LICENSE_AGREEMENT=yes \
    --env=NEO4J_dbms_memory_pagecache_size=1G \
    -v $PWD/plugins:/plugins \
    neo4j:3.3.0-enterprise
