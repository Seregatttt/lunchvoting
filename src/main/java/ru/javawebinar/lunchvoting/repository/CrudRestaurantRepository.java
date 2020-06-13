package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant t WHERE t.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT m FROM Restaurant m LEFT JOIN FETCH m.menus WHERE m.id = ?1")
    Restaurant getWithMenus(int id);

//    //@EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT r FROM Restaurant r  LEFT JOIN FETCH  r.menus ")// +
//           // " WHERE r.menus.dateMenu = :dateMenu" +
//          //  " ORDER BY m.dateMenu DESC")
//    List<Restaurant> findAllWithMenusAndMeals();
}