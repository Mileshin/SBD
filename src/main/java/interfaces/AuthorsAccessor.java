package interfaces;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import entity.Authors;
import entity.Spaces;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Accessor
public interface AuthorsAccessor {
    @Query("SELECT * FROM authors")
    Result<Authors> getAll();

    @Query("SELECT * FROM authors WHERE login = :n")
    Result<Authors> getAllByLogin(@Param("n") String login);

    @Query("SELECT * FROM authors where login = :n ORDER BY modified DESC LIMIT :m")
    Result<Authors> getLastRowsByLogin(@Param("n") String login, @Param("m") int count);

    @Query("SELECT * FROM authors WHERE login = :n AND modified > :m AND modified < :k")
    Result<Authors> getAllBetweenTime(@Param("n") String login, @Param("m") Date time1, @Param("k") Date time2);

    @Query("INSERT INTO authors(modified,login,table_name,action)values(dateof(now()),?,?,?)")
    ResultSet insertNow(String login, String tableName, String action);



}
