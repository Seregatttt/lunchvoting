package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.CrudRestaurantRepository;
import ru.javawebinar.lunchvoting.to.RestaurantTo;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService restaurantService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    private CrudRestaurantRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void create() {
        Restaurant newRest = NEW_REST;
        Restaurant created = restaurantService.createOrUpdate(new Restaurant(newRest));
        int newId = created.getId();
        newRest.setId(newId);
        assertEquals(created, newRest);
        assertEquals(restaurantService.get(newId), newRest);
    }

    @Test
    void getAll() {
        List<Restaurant> all = restaurantService.getAll();
        assertEquals(all, RESTAURANTS);
    }

    @Test
    void get() {
        Restaurant restaurant = restaurantService.get(REST1.getId());
        assertEquals(restaurant, REST1);
    }

    @Test
    void getWithMenus() throws Exception {
        Restaurant actual = restaurantService.getWithMenus(REST.getId());
        REST_MATCHER.assertMatch(actual, REST);
        List<Menu> menus = List.of(MENU, MENU3, MENU6);
        MENU_MATCHER.assertMatch(actual.getMenus(), menus);
    }

    @Test
    void getAllWithMenuAndMealsByDate() throws Exception {
        List<RestaurantTo> restaurantTos = restaurantService.getAllWithMenuAndMealsByDate(LOCAL_DATE);
        assertEquals(restaurantTos.size(), 3);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(1));
    }

    @Test
    void update() {
        Restaurant updated = UPDATE_REST2_ADDRESS;
        restaurantService.createOrUpdate(new Restaurant(updated));
        assertEquals(restaurantService.get(REST2.getId()), updated);
    }

    @Test
    public void delete() {
        restaurantService.delete(REST1.getId());
        Assertions.assertNull(repository.findById(REST1.getId()).orElse(null));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.delete(1));
    }
}