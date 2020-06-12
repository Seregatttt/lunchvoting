package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.repository.CrudUserRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CrudUserRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    void create() {
        User newUser = NEW_USER;
        User created = service.create(new User(newUser));
        int newId = created.getId();
        newUser.setId(newId);
        assertEquals(created, newUser);
        assertEquals(service.get(newId), newUser);
    }

    @Test
    void createDuplicateMail() {
        assertThrows(DataAccessException.class, () ->
                service.create(NEW_USER_DOUBLE_EMAIL));
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        assertEquals(all, of(ADMIN, USER, USER2, USER3));
    }

    @Test
    void get() {
        User user = service.get(ADMIN.getId());
        assertEquals(user, ADMIN);
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@mail.ru");
        assertEquals(user, ADMIN);
    }

    @Test
    void getNotFoundByEmail() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("ad-min@mail.ru"));
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(1));
    }

    @Test
    void update() {
        User updated = UPDATE_ADMIN_NEW_EMAIL;
        service.update(new User(updated));
        assertEquals(service.get(100), updated);
    }

    @Test
    void updateDublicateEmail() {
        User updated = NEW_USER_DOUBLE_EMAIL;
        assertThrows(DataAccessException.class, () ->
                service.update(new User(updated)));
    }

    @Test
    public void delete() {
        service.delete(101);
        Assertions.assertNull(repository.findById(101).orElse(null));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1));
    }

    @Test
    void deleteUseCache() {
        List<User> all = service.getAll();
        assertEquals(all, of(ADMIN, USER, USER2, USER3));
        service.deleteUseCache(101);
        List<User> all2 = service.getAll();
        assertEquals(all2, of(ADMIN, USER, USER2, USER3));
    }
}