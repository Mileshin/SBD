package interfaces;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import entity.Spaces;

/**
 * Created by Andrey on 06.04.2018.
 */
@Accessor
public interface PagesAccessor {
    @Query("SELECT * FROM pages")
    Result<Spaces> getAll();

    @Query("SELECT * FROM pages WHERE name = :n")
    Result<Spaces> getAllByName(@Param("n") String name);

    @Query("SELECT * FROM pages where name = :n ORDER BY revision DESC LIMIT :m")
    Result<Spaces> getLastRevisionsByName(@Param("n") String name, @Param("m") int count);

    @Query("SELECT * FROM pages WHERE name = :n AND revision > :m AND revision < :k")
    Result<Spaces> getAllBetweenRevision(@Param("n") String name, @Param("m") int rev1, @Param("k") int re2);

    /*@Query("SELECT * FROM spaces WHERE name = :n AND modified > :m AND modified < :k")
    Result<Spaces> getAllBetweenTime(@Param("n") String name,@Param("m") Date time1,@Param("k") Date time2);*/


}
