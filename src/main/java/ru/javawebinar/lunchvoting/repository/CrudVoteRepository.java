package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Vote;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote t WHERE t.id=:id and t.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Vote m WHERE m.user.id=:userId ORDER BY m.dateLunch DESC")
    List<Vote> getAll(@Param("userId") int userId);

    @Query("SELECT m FROM Vote m JOIN FETCH m.menu WHERE m.id = ?1 and m.user.id = ?2")
    Vote getWithMenu(int id, int userId);

    @Query("SELECT m FROM Vote m JOIN FETCH m.user WHERE m.id = ?1 and m.user.id = ?2")
    Vote getWithUser(int id, int userId);

    //@EntityGraph(attributePaths = {"menus","users"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Vote m JOIN FETCH m.user JOIN FETCH m.menu WHERE m.id = ?1 and m.user.id = ?2")
    Vote getWithUserAndMenu(int id, int userId);
}