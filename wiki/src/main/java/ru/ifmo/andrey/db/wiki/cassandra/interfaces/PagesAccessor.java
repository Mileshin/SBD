package ru.ifmo.andrey.db.wiki.cassandra.interfaces;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Pages;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Accessor
public interface PagesAccessor {
    @Query("SELECT * FROM pages")
    @QueryParameters(consistency="ONE")
    Result<Pages> getAll();

    @Query("SELECT * FROM pages WHERE name = :n")
    @QueryParameters(consistency="ONE")
    Result<Pages> getAllByName(@Param("n") String name);

    @Query("SELECT * FROM pages where name = :n ORDER BY modificationTime DESC LIMIT :m")
    @QueryParameters(consistency="ONE")
    Result<Pages> getLastRowsByName(@Param("n") String name, @Param("m") int count);

    @Query("SELECT * FROM pages WHERE name = :n AND modificationTime > :m AND modificationTime < :k")
    @QueryParameters(consistency="ONE")
    Result<Pages> getAllBetweenTime(@Param("n") String name, @Param("m") Date time1, @Param("k") Date time2);

    @Query("INSERT INTO pages(modificationTime, name, content, author) values (toTimestamp(now()), ?, ?, ?)")
    @QueryParameters(consistency="ONE")
    ResultSet insertNow(String name, String content, String author);
}
