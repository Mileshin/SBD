package ru.ifmo.andrey.db.wiki.cassandra.interfaces;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Authors;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Accessor
public interface AuthorsAccessor {
    @Query("SELECT * FROM authors")
    @QueryParameters(consistency="ONE")
    Result<Authors> getAll();

    @Query("SELECT * FROM authors WHERE login = :n")
    @QueryParameters(consistency="ONE")
    Result<Authors> getAllByLogin(@Param("n") String login);

    @Query("SELECT * FROM authors where login = :n ORDER BY modificationTime DESC LIMIT :m")
    @QueryParameters(consistency="ONE")
    Result<Authors> getLastRowsByLogin(@Param("n") String login, @Param("m") int count);

    @Query("SELECT * FROM authors WHERE login = :n AND modificationTime > :m AND modificationTime < :k")
    @QueryParameters(consistency="ONE")
    Result<Authors> getAllBetweenTime(@Param("n") String login, @Param("m") Date time1, @Param("k") Date time2);

    @Query("INSERT INTO authors(modificationTime, login, tableName, action) values (toTimestamp(now()), ?, ?, ?)")
    @QueryParameters(consistency="ONE")
    ResultSet insertNow(String login, String tableName, String action);
}
