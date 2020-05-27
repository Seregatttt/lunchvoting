package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Meal;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.menu.id=:menuId")
    int delete(@Param("id") int id, @Param("menuId") int menuId);

    @Query("SELECT m FROM Meal m WHERE m.menu.id=:menuId ORDER BY m.id")
    List<Meal> getAll(@Param("menuId") int menuId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.menu WHERE m.id = ?1 ")
    Meal getWithMenu(int id);
}