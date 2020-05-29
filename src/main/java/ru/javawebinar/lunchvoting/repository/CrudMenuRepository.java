package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restId")
    int delete(@Param("id") int id, @Param("restId") int restId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restId ORDER BY m.id")
    List<Menu> getAll(@Param("restId") int restId);

    @Query("SELECT m FROM Menu m WHERE m.id = ?1")
    Menu get(int id);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.meals WHERE m.id = ?1")
    Menu getWithMeals(int id);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = ?1 and m.restaurant.id = ?2 ")
    Menu getWithRest(int id, int restId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant LEFT JOIN FETCH m.meals WHERE m.id = ?1 and m.restaurant.id = ?2 ")
    Menu getWithRestAndMeals(int id, int restId);

    @Query("SELECT m from Menu m WHERE m.restaurant.id=:restId AND m.dateMenu >= :startDate "
            + " AND m.dateMenu <= :endDate ORDER BY m.dateMenu DESC")
    List<Menu> getBetweenInclude(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restId") int restId);
}