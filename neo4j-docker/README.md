# Информация по развертыванию neo4j в docker
### docker-compose 
**docker-compose.yml** - конфиг для запуска кластера через docker-compose. Команда для запуска
```sh
docker-compose --file SBD/neo4j-docker/neo4j-docker-compose.yml up -d
```

### haproxy
В **haproxy.cfg** - настройки для haproxy. Подключение должно быть к порту 8484.

В **create.sh ** находится скрипт для состания HA кластера анологичный тому, что написан в [документации](https://neo4j.com/docs/operations-manual/current/installation/docker/).  Хрен знает посему это не работает. Возможно там нужно как-то специфически строить сеть, но через overlay тоже не получилось.
