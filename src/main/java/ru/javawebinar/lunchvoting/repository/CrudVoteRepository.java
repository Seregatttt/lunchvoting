package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote t WHERE t.id=:id and t.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM Vote t WHERE t.id=:id and t.user.id = :userId")
//    int deleteByIdUserId(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Vote m JOIN FETCH m.restaurant  " +
            " WHERE m.user.id=:userId AND m.dateVote >= :startDate " +
            " AND m.dateVote <= :endDate ORDER BY m.dateVote DESC")
    List<Vote> getLunchVotesBetweenInclude(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate,
                                           @Param("userId") int userId);

    @Query("SELECT t FROM Vote t WHERE t.id = :id and t.user.id=:userId ")
    Vote get(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT t FROM Vote t WHERE t.user.id = :userId and t.restaurant.id=:restaurantId and t.dateVote = :dateVote")
    Vote get(@Param("userId") int userId, @Param("restaurantId") int restaurantId, @Param("dateVote") LocalDate dateVote);

    @Query("SELECT t FROM Vote t JOIN FETCH t.user WHERE t.dateVote=:dateVote and t.user.id = :userId")
    Vote getByDateLunch(@Param("dateVote") LocalDate dateVote, @Param("userId") int userId);

//    @Query("SELECT m FROM Vote m JOIN FETCH m.menu WHERE m.menu.id = ?1 and m.user.id = ?2")
//    Vote getWithMenu(int menuId, int userId);
//
//    @Query("SELECT m FROM Vote m JOIN FETCH m.user WHERE m.menu.id = ?1 and m.user.id = ?2")
//    Vote getWithUser(int menuId, int userId);
//
//    //@EntityGraph(attributePaths = {"menus","users"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT m FROM Vote m JOIN FETCH m.user JOIN FETCH m.menu WHERE m.menu.id = ?1 and m.user.id = ?2")
//    Vote getWithUserAndMenu(int menuId, int userId);
}