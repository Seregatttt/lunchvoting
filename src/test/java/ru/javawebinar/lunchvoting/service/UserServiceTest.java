package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.lunchvoting.model.Role;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.repository.UserRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

//import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractServiceTest {
    public static User ADMIN = new User(100, "Admin", "admin@mail.com", "admin", Role.ADMIN);
    public static User USER1 = new User(101, "User1", "user1@mail.ru", "user1", Role.USER);
    public static User USER2 = new User(102, "User2", "user2@mail.ru", "user2", Role.USER);
    public static User NEW_USER1 = new User(101, "new_user", "new_user@mail.ru", "new_user1", Role.USER);

    @Autowired
    protected UserService service;

    @Autowired
    private UserRepository repository;

    @Test
    void create() {
        User newUser = NEW_USER1;
        User created = service.create(new User(newUser));
        int newId = created.getId();
        newUser.setId(newId);
        assertEquals(created, newUser);
        assertEquals(service.get(newId), newUser);
    }

    @Test
    void createDuplicateMail() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user1@mail.ru", "newPass", Role.USER)));
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        assertEquals(all, of(ADMIN, USER1, USER2));
    }

    @Test
    void get() {
        User user = service.get(ADMIN.getId());
        assertEquals(user, ADMIN);
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@mail.com");
        //  USER_MATCHER.assertMatch(user, 100);
    }

    @Test
    void getNotFoundByEmail() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("admin@mail.ru"));
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(1));
    }

    @Test
    void update() {
        User updated = new User(100, "Admin", "super-admin@mail.com", "admin", Role.ADMIN);
        service.update(new User(updated));
        assertEquals(service.get(100), updated);
    }

    @Test
    void updateDublicateEmail() {
        User updated = new User(100, "Admin", "user1@mail.ru", "admin", Role.ADMIN);
        assertThrows(DataAccessException.class, () ->
                service.update(new User(updated)));
    }

    @Test
    public void delete() {
        service.delete(101);
        Assertions.assertNull(repository.get(101));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1));
    }

//    @Test
//    void createWithException() {
//        validateRootCause(() -> service.create(new User(null, "  ", "user1@mail.ru", "password", Role.USER)), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.USER)), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new User(null, "User", "user1@mail.ru", "  ", Role.USER)), ConstraintViolationException.class);
//    }


}