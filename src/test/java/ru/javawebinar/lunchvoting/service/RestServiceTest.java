package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.RestRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.DataForTest.*;

public class RestServiceTest extends AbstractServiceTest {
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