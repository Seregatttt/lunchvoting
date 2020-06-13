package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
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
    @Query("DELETE FROM Vote v WHERE v.id=:id and v.user.id = :userId ")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id=:userId ")
    Vote get(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id=:userId and v.restaurant.id= :restaurantId")
    Vote get(@Param("id") int id, @Param("userId") int userId, @Param("restaurantId") int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId and v.restaurant.id=:restaurantId and v.dateVote = :dateVote")
    Vote get(@Param("userId") int userId, @Param("restaurantId") int restaurantId, @Param("dateVote") LocalDate dateVote);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user WHERE v.dateVote=:dateVote and v.user.id = :userId")
    Vote getByDateLunch(@Param("dateVote") LocalDate dateVote, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v " +
            " WHERE v.user.id=:userId AND v.dateVote >= :startDate " +
            " AND v.dateVote <= :endDate ORDER BY v.dateVote DESC")
    List<Vote> getUserVotesBetweenInclude(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.dateVote=:dateVote ")
    List<Vote> findAllByDateVote(@Param("dateVote") LocalDate dateVote);
}