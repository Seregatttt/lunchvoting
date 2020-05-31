package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.repository.MealRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.TestData.MEAL_MATCHER;
import static ru.javawebinar.lunchvoting.web.TestData.MENU_MATCHER;


public class MealServiceTest extends AbstractServiceTest {
    public static final int MEAL1_ID = 1003;
    public static final int MENU_ID_MEAL1_ID = 10001;
    public static final Menu MENU1 = new Menu(MENU_ID_MEAL1_ID,  of(2020, Month.MAY, 01));
    public static final Meal MEAL1 = new Meal(1003, "cake", 1.05f);//(10001, 'cake', 1.05), menu= (11, '2020-05-01'),--10001
    public static final Meal MEAL2 = new Meal(1004, "tea", 3.05f);//(10001, 'tea', 3.05),
    public static final List<Meal> MEALS = List.of(MEAL1, MEAL2);

    @Autowired
    protected MealService service;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected MealRepository repository;

    @Autowired
    protected CacheManager cacheManager;

    @Test
    void create() throws Exception {
        Meal newMeal = new Meal(null, "new meal", 5.55f);
        Meal created = service.create(newMeal, MENU_ID_MEAL1_ID);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, MENU_ID_MEAL1_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        List<Meal> meals = service.getAll(MENU_ID_MEAL1_ID);
        assertEquals(meals.size(), 2);
        MEAL_MATCHER.assertMatch(meals, MEALS);
    }

    @Test
    void get() throws Exception {
        Meal actual = service.get(MEAL1_ID, MENU_ID_MEAL1_ID);
        MEAL_MATCHER.assertMatch(actual, MEAL1);
    }

    @Test
    void getWithMenu() throws Exception {
        Meal actual = service.getWithMenu(MEAL1_ID, MENU_ID_MEAL1_ID);
        MEAL_MATCHER.assertMatch(actual, MEAL1);
        MENU_MATCHER.assertMatch(actual.getMenu(), MENU1);
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
        Meal updated = new Meal(MEAL1_ID, "update salad", 555f);
        service.update(updated, 10001);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, 10001), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.update(MEAL1, 11));
    }

    @Test
    void delete() throws Exception {
        service.delete(MEAL1_ID, MENU_ID_MEAL1_ID);
        Assertions.assertNull(repository.get(MEAL1_ID, MENU_ID_MEAL1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.delete(1, MENU_ID_MEAL1_ID));
    }

    @Test
    void deleteNotMenu() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.delete(MEAL1_ID, -1));
    }

//    @Test
//    void createWithException() throws Exception {
//        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "  ", 300), USER_ID), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new Meal(null, null, "Description", 300), USER_ID), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 9), USER_ID), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 5001), USER_ID), ConstraintViolationException.class);
//    }

}