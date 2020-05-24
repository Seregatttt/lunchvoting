package ru.javawebinar.lunchvoting.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.repository.Repository;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.repository.MenuRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.TestData.*;
import static java.time.LocalDate.of;

public class MenuServiceTest extends AbstractServiceTest {
    public static final int REST_ID_MENU = 10;
    public static final int MENU_ID = 10000;//(10, '2020-05-01'),--10001
    public static final int MENU3_ID = 10003;// (10, '2020-05-02'),--10003
    public static final int MENU6_ID = 10006;//  (10, '2020-05-03');--10006
    public static final Menu MENU = new Menu(MENU_ID,  of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(MENU3_ID,  of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(MENU6_ID,  of(2020, Month.MAY, 03));
    public static final List<Menu> MENUS = List.of(MENU, MENU3,MENU6);

    @Autowired
    protected MenuService service;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MenuRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    void create() throws Exception {
        Menu newMenu = new Menu(null,  of(2020, Month.MAY, 05));
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
        Menu updated = new Menu(MENU_ID,  of(2020, Month.MAY, 11));
        service.update(updated, REST_ID_MENU);
        MENU_MATCHER.assertMatch(service.get(MENU_ID, REST_ID_MENU), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.update(MENU, 11));
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


//    @Test
//    void getBetweenWithNullDates() throws Exception {
//        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
//    }
//
//    @Test
//    void getBetweenInclusive() throws Exception {
//        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
//                LocalDate.of(2020, Month.JANUARY, 30),
//                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
//                MEAL3, MEAL2, MEAL1);
//    }
}