package interfaces;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;
import entity.Spaces;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Accessor
public interface PagesAccessor {
    @Query("SELECT * FROM pages")
    @QueryParameters(consistency="ONE")
    Result<Spaces> getAll();

    @Query("SELECT * FROM pages WHERE name = :n")
    @QueryParameters(consistency="ONE")
    Result<Spaces> getAllByName(@Param("n") String name);

    @Query("SELECT * FROM pages where name = :n ORDER BY modified DESC LIMIT :m")
    @QueryParameters(consistency="ONE")
    Result<Spaces> getLastRowsByName(@Param("n") String name, @Param("m") int count);

    @Query("SELECT * FROM pages WHERE name = :n AND modified > :m AND modified < :k")
    @QueryParameters(consistency="ONE")
    Result<Spaces> getAllBetweenTime(@Param("n") String name, @Param("m") Date time1, @Param("k") Date time2);

    @Query("INSERT INTO pages(modified,name,content,diffs_json)values(dateof(now()),?,?,?)")
    @QueryParameters(consistency="ONE")
    ResultSet insertNow(String name, String content, String diff_json);


}
