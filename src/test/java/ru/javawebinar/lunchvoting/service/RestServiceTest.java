package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.RestRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.TestData.*;

public class RestServiceTest extends AbstractServiceTest {
    public static final Restaurant NEW_REST = new Restaurant(null, "New Restaurant", "Moscow");
    public static final Restaurant REST = new Restaurant(10, "Celler de Can Roca", "Spain");
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    public static final Restaurant UPDATE_REST2_ADDRESS = new Restaurant(12, "Sato", "Update city");
    public static final List<Restaurant> RESTAURANTS = List.of(REST, REST1, REST2);
    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    @Autowired
    protected RestService service;

    @Autowired
    private RestRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void create() {
        Restaurant newRest = NEW_REST;
        Restaurant created = service.create(new Restaurant(newRest));
        int newId = created.getId();
        newRest.setId(newId);
        assertEquals(created, newRest);
        assertEquals(service.get(newId), newRest);
    }

    @Test
    void getAll() {
        List<Restaurant> all = service.getAll();
        assertEquals(all, RESTAURANTS);
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(REST1.getId());
        assertEquals(restaurant, REST1);
    }

    @Test
    void getWithMenus() throws Exception {
        Restaurant actual = service.getWithMenus(REST.getId());
        REST_MATCHER.assertMatch(actual, REST);
        List<Menu> menus = List.of(MENU,MENU3,MENU6);
        MENU_MATCHER.assertMatch(actual.getMenus(), menus);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(1));
    }

    @Test
    void update() {
        Restaurant updated = UPDATE_REST2_ADDRESS;
        service.update(new Restaurant(updated));
        assertEquals(service.get(REST2.getId()), updated);
    }

    @Test
    public void delete() {
        service.delete(REST1.getId());
        Assertions.assertNull(repository.get(REST1.getId()));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1));
    }
}