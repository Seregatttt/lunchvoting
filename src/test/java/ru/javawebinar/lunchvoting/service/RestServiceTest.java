package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.RestRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestServiceTest extends AbstractServiceTest {
    public static final Restaurant NEW_REST = new Restaurant(null, "New Restaurant", "Moscow");
    public static final Restaurant REST = new Restaurant(10, "Celler de Can Roca", "Spain");
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    public static final Restaurant UPDATE_REST2_ADDRESS = new Restaurant(12, "Sato", "Update city");
    public static final List<Restaurant> RESTAURANTS = List.of(REST, REST1, REST2);

    @Autowired
    protected RestService service;

    @Autowired
    private RestRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

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