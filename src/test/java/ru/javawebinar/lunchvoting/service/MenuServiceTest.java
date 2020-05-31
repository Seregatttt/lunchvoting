package ru.javawebinar.lunchvoting.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.repository.MenuRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.DataForTest.*;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    protected MenuService service;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MenuRepository repository;

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
        List<Menu> menus = service.getBetweenInclude(null, null);
        MENU_MATCHER.assertMatch(menus,
                List.of(MENU6, MENU5, MENU4, MENU3, MENU2, MENU1, MENU));
    }
}