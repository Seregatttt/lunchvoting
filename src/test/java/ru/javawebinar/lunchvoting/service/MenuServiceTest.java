package ru.javawebinar.lunchvoting.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.MenuRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.TestData.*;

public class MenuServiceTest extends AbstractServiceTest {
    public static final int REST_ID_MENU = 10;
    public static final int MENU_ID = 10000;//(10, '2020-05-01'),--10001
    public static final int MENU3_ID = 10003;// (10, '2020-05-02'),--10003
    public static final int MENU6_ID = 10006;//  (10, '2020-05-03');--10006
    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU1 = new Menu(10001, of(2020, Month.MAY, 01));
    public static final Menu MENU2 = new Menu(10002, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    public static final Menu MENU4 = new Menu(10004, of(2020, Month.MAY, 02));
    public static final Menu MENU5 = new Menu(10005, of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(MENU6_ID, of(2020, Month.MAY, 03));
    public static final List<Menu> MENUS = List.of(MENU, MENU3, MENU6);
    public static final Meal MEAL1 = new Meal(1000, "Salad", 5.50f);
    public static final Meal MEAL2 = new Meal(1001, "juice", 4.50f);
    public static final Meal MEAL3 = new Meal(1002, "soup", 3.05f);
    public static final Restaurant REST = new Restaurant(10, "Celler de Can Roca", "Spain");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    public static final int MENU2_ID = 10002;//(12, '2020-05-01'),--10002   (10002, 'coffee', 4.05), (10002, 'ice cream', 5.05),
    public static final Menu MENU_WITH_REST = new Menu(MENU_ID, REST, of(2020, Month.MAY, 01));
    @Autowired
    protected MenuService service;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MenuRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void create() throws Exception {
        Menu newMenu = new Menu(null, of(2020, Month.MAY, 05));
        newMenu.setRestaurant(REST);
        Menu created = service.create(newMenu, REST_ID_MENU);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(service.get(newId, REST_ID_MENU), newMenu);
    }

    @Test
    void getAll() throws Exception {
        List<Menu> menus = service.getAll(REST_ID_MENU);
        assertEquals(menus.size(), 3);
        MENU_MATCHER.assertMatch(menus, MENUS);
    }

    @Test
    void get() throws Exception {
        Menu actual = service.get(MENU_ID, REST_ID_MENU);
        MENU_MATCHER.assertMatch(actual, MENU);
    }

    @Test
    void getWithMeals() throws Exception {
        Menu actual = service.getWithMeals(MENU_ID, REST_ID_MENU);
        MENU_MATCHER.assertMatch(actual, new Menu(MENU_ID, of(2020, Month.MAY, 01)));
        List<Meal> meals = List.of(MEAL1, MEAL2, MEAL3);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    void getWithRestAndMeals() throws Exception {
        Menu actual = service.getWithRestAndMeals(MENU_ID, 10);
        MENU_MATCHER.assertMatch(actual, new Menu(MENU_ID, of(2020, Month.MAY, 01)));
        REST_MATCHER.assertMatch(actual.getRestaurant(), REST);
        List<Meal> meals = List.of(MEAL1, MEAL2, MEAL3);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    void getWithRest() throws Exception {
        Menu actual = service.getWithRest(MENU_ID, 10);
        MENU_MATCHER.assertMatch(actual, new Menu(MENU_ID, of(2020, Month.MAY, 01)));
        REST_MATCHER.assertMatch(actual.getRestaurant(), REST);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.get(2, REST_ID_MENU));
    }

    @Test
    void getNotRestId() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.get(MENU_ID, 555));
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(10002, of(2020, Month.MAY, 11));
        updated.setRestaurant(REST2);
        service.update(updated, REST2.getId());
        MENU_MATCHER.assertMatch(service.get(10002, 12), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.update(MENU_WITH_REST, 1));
    }

    @Test
    void delete() throws Exception {
        service.delete(MENU_ID, REST_ID_MENU);
        Assertions.assertNull(repository.get(MENU_ID, REST_ID_MENU));
    }

    @Test
    void deleteNotFoundId() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.delete(1, REST_ID_MENU));
    }

    @Test
    void deleteNotMenu() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.delete(MENU_ID, -1));
    }

    @Test
    void getBetweenInclude() throws Exception {
        MENU_MATCHER.assertMatch(service.getBetweenInclude(
                LocalDate.of(2020, Month.MAY, 02),
                LocalDate.of(2020, Month.MAY, 02)), List.of(MENU5, MENU4, MENU3));
    }

    @Test
    void getBetweenWithNullDates() throws Exception {
        MENU_MATCHER.assertMatch(service.getBetweenInclude(null, null),
                List.of(MENU6, MENU5, MENU4, MENU3, MENU2, MENU1, MENU));
    }
}