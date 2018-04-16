# Информация по развертыванию cassandra в docker
В теории можно подключатся к любому узлу кластера кассандры и работать через него. Но чтобы избежать лишней мороки, был создан специальный узел, который выступает, как координатор. На этом узле нечего хранится не будет, но на него будут привязаны все другие узлы.

Как примерно должно выглядеть пространство ключей:
``` sql
CREATE KEYSPACE test WITH replication = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1, 'datacenter2' : 1, 'datacenter3' : 1 };
```

Подключится к кассандре с помощью cqlsh
``` sh
docker run -it --link container_name:cassandra --rm cassandra sh -c 'exec cqlsh "$CASSANDRA_PORT_9042_TCP_ADDR"'
# или непосредственно с самого узла
docker exec -ti container_name cqlsh
```

Посмотреть логи определенного контейнера
``` sh
docker logs container_name
``` 

Посмотерть логи кассандры можно через bash
``` sh
docker exec -it container_name bash
```

Если вылезла ошибка **Exception encountered during startup: Other bootstrapping/leaving/moving nodes detected, cannot bootstrap while cassandra.consistent.rangemovement is true.** Просто запустите контейнер повторно.
