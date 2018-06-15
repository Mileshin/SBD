## Описание API
* Все запросы, возвращающие множество объектов, поддерживают параметры `?page=...` и `?count=...`
* **PUT**, **DELETE** и некоторые **POST**-запросы принимают обязательный параметр `?author=...`, определяющий сущность, совершающую действие

### GET запросы
#### MongoDB
Получить все страницы, MongoDB<br>
`GET /pages`

Получить страницу, MongoDB<br>
`GET /pages/<page name>`

Получить все вложения, MongoDB<br>
`GET /attachments`

Получить вложение, MongoDB<br>
`GET /attachments/<name>`

Получить все пространства, MongoDB<br>
`GET /spaces`

Получить всех авторов, MongoDB<br>
`GET /authors`

Получить автора, MongoDB<br>
`GET /authors/<name>`

Получить страницы, содержащие в тексте указанную подстроку, MongoDB<br>
`GET /pages?text=...`

Получить пространство, MongoDB<br>
`GET /spaces/<space>`
#### Cassandra
Получить историю изменений страницы, Cassandra<br>
`GET /pages/<page name>/log`

Получить разницу между ревизиями страницы, Cassandra<br>
`GET /pages/<page name>/diff?from=<rev>&to=<rev>`

Получить историю изменений пространства, Cassandra<br>
`GET /spaces/<space name>/log`

Получить разницу между ревизиями пространства, Cassandra<br>
`GET /spaces/<space name>/diff?from=<rev>&to=<rev>`

Получить лог действий автора, Cassandra<br>
`GET /authors/<name>/log`

#### Neo4j
Получить страницы, ссылающиеся на данную, Neo4j<br>
`GET /pages/<page name>/referred-by`

Получить все страницы в пространстве, Neo4j<br>
`GET /spaces/<space>/pages`

Получить все страницы, созданные автором, Neo4j<br>
`GET /authors/<name>/authored-pages`

Получить все страницы, прокомментированные автором, Neo4j<br>
`GET /authors/<name>/commented-pages`

Получить все вложения, загруженные автором, Neo4j<br>
`GET /authors/<name>/attachments`

Получить страницы, ссылающиеся на вложение, Neo4j<br>
`GET /attachments/<name>/referred-by`

### DELETE запросы
Удалить страницу, MongoDB && Neo4j && Cassandra<br>
`DELETE /pages/<page name>`

Удалить пространство, MongoDB && Neo4j && Cassandra<br>
`DELETE /spaces/<space>`

Удалить автора, MongoDB && Neo4j && Cassandra<br>
`DELETE /authors/<name>`

Удалить вложение, MongoDB && Neo4j && Cassandra<br>
`DELETE /attachments/<name>`

### POST/PUT запросы
Создать/обновить страницу, MongoDB && Neo4j && Cassandra<br>
`POST/PUT /pages/<page name>`

Создать/обновить пространство, MongoDB && Neo4j && Cassandra<br>
`POST/PUT /spaces/<space>`

Создать/обновить автора, MongoDB && Neo4j && Cassandra<br>
`POST/PUT /authors/<name>`
