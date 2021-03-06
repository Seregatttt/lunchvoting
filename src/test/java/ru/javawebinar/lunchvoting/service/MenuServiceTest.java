package ru.javawebinar.lunchvoting.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    protected MenuService service;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private CrudMenuRepository repository;

    @Test
    void create() throws Exception {
        Menu newMenu = new Menu(null, of(2020, Month.MAY, 5));
        newMenu.setRestaurant(REST);
        Menu created = service.createOrUpdate(newMenu, REST_ID_MENU);
        int newId = created.getId();
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
        MENU_MATCHER.assertMatch(actual, new Menu(MENU_ID, of(2020, Month.MAY, 1)));
        List<Meal> meals = List.of(MEAL, MEAL1, MEAL2);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    void getAllWithMenuByDate() throws Exception {
        List<Menu> actual = service.getAllByDateWithRestAndMeals(LOCAL_DATE);
        MENU_MATCHER.assertMatch(actual, List.of(MENU, MENU1, MENU2));
    }

    @Test
    void getAllByDateWithRestAndMeals() throws Exception {
        Menu actual = service.getWithRestAndMeals(MENU_ID, REST_ID_MENU);
        MENU_MATCHER.assertMatch(actual, new Menu(MENU_ID, of(2020, Month.MAY, 1)));
        REST_MATCHER.assertMatch(actual.getRestaurant(), REST);
        List<Meal> meals = List.of(MEAL, MEAL1, MEAL2);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    void getWithRest() throws Exception {
        Menu actual = service.getWithRest(MENU_ID, REST_ID_MENU);
        MENU_MATCHER.assertMatch(actual, new Menu(MENU_ID, of(2020, Month.MAY, 1)));
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
        Menu updated = new Menu(10009, of(2020, Month.MAY, 11));
        updated.setRestaurant(REST2);
        service.createOrUpdate(updated, REST2.getId());
        MENU_MATCHER.assertMatch(service.get(10009, REST2.getId()), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.createOrUpdate(MENU_WITH_REST, 1));
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
                LocalDate.of(2020, Month.MAY, 2),
                LocalDate.of(2020, Month.MAY, 2)), List.of(MENU5, MENU4, MENU3));
    }

    @Test
    void getBetweenWithNullDates() throws Exception {
        List<Menu> menus = service.getBetweenInclude(null, null);
        MENU_MATCHER.assertMatch(menus,
                List.of(MENU6, MENU5, MENU4, MENU3, MENU2, MENU1, MENU));
    }
}