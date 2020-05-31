package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.repository.MealRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.DataForTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.DataForTest.MEAL_MATCHER;
import static ru.javawebinar.lunchvoting.web.DataForTest.MENU_MATCHER;


public class MealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected MealRepository repository;

    @Test
    void create() throws Exception {
        Meal newMeal = new Meal(null, "new meal", 5.55f);
        Meal created = service.create(newMeal, DataForTest.MENU_ID_MEAL1_ID);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, DataForTest.MENU_ID_MEAL1_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        List<Meal> meals = service.getAll(DataForTest.MENU_ID_MEAL1_ID);
        assertEquals(meals.size(), 2);
        MEAL_MATCHER.assertMatch(meals, List.of(DataForTest.MEAL3, DataForTest.MEAL4));
    }

    @Test
    void get() throws Exception {
        Meal actual = service.get(DataForTest.MEAL3_ID, DataForTest.MENU_ID_MEAL1_ID);
        MEAL_MATCHER.assertMatch(actual, DataForTest.MEAL3);
    }

    @Test
    void getWithMenu() throws Exception {
        Meal actual = service.getWithMenu(DataForTest.MEAL3_ID, DataForTest.MENU_ID_MEAL1_ID);
        MEAL_MATCHER.assertMatch(actual, DataForTest.MEAL3);
        MENU_MATCHER.assertMatch(actual.getMenu(), DataForTest.MENU1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.get(2, 2));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.get(1001, 555));
    }

    @Test
    void update() throws Exception {
        Meal updated = new Meal(DataForTest.MEAL3_ID, "update salad", 555f);
        service.update(updated, 10001);
        MEAL_MATCHER.assertMatch(service.get(DataForTest.MEAL3_ID, 10001), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.update(DataForTest.MEAL1, 11));
    }

    @Test
    void delete() throws Exception {
        service.delete(DataForTest.MEAL3_ID, DataForTest.MENU_ID_MEAL1_ID);
        Assertions.assertNull(repository.get(DataForTest.MEAL3_ID, DataForTest.MENU_ID_MEAL1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.delete(1, DataForTest.MENU_ID_MEAL1_ID));
    }

    @Test
    void deleteNotMenu() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.delete(DataForTest.MEAL3_ID, -1));
    }
}