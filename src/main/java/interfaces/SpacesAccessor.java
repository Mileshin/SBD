package interfaces;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import entity.Spaces;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Accessor
public interface SpacesAccessor {
    @Query("SELECT * FROM spaces")
    Result<Spaces> getAll();

    @Query("SELECT * FROM spaces WHERE name = :n")
    Result<Spaces> getAllByName(@Param("n") String name);

    @Query("SELECT * FROM spaces where name = :n ORDER BY modified DESC LIMIT :m")
    Result<Spaces> getLastRowsByName(@Param("n") String name, @Param("m") int count);

    @Query("SELECT * FROM spaces WHERE name = :n AND modified > :m AND modified < :k")
    Result<Spaces> getAllBetweenTime(@Param("n") String name, @Param("m") Date time1, @Param("k") Date time2);

    @Query("INSERT INTO spaces(modified,name,description,diffs_json,parent)values(dateof(now()),?,?,?,?)")
    ResultSet insertNow(String name, String description, String diff_json, String parent);

}
