package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.lunchvoting.model.*;
import ru.javawebinar.lunchvoting.repository.RestRepository;
import ru.javawebinar.lunchvoting.repository.VoteRepository;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.TestData.*;

public class VoteServiceTest extends AbstractServiceTest {

    public static final Restaurant REST = new Restaurant(10, "Celler de Can Roca", "Spain");
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    public static final Restaurant UPDATE_REST2_ADDRESS = new Restaurant(12, "Sato", "Update city");
    public static final List<Restaurant> RESTAURANTS = List.of(REST, REST1, REST2);

    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));

    public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User USER1 = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static final Vote NEW_VOTE = new Vote(null, USER2, MENU6);
    public static final Vote VOTE = new Vote(0, USER1, MENU);
    public static final Vote VOTE2 = new Vote(2, USER1, MENU3);

    @Autowired
    protected VoteService service;

    @Autowired
    private VoteRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void create() {
        Vote create = NEW_VOTE;
        Vote created = service.create(10006, 102);
        int newId = created.getId();
        create.setId(newId);
        assertEquals(created, create);
        assertEquals(service.get(10006, 102), create);
    }

    @Test
    void getAll() {
        List<Vote> all = service.getAll(101);
        assertEquals(all, List.of(VOTE2, VOTE));
    }

    @Test
    void get() {
        Vote vote = service.get(10000,101);
        assertEquals(vote, VOTE);
    }

    @Test
    void getWithUserAndMenu() throws Exception {
        Vote actual = service.getWithUserAndMenu(10000,101);
        VOTE_MATCHER.assertMatch(actual, VOTE);
        MENU_MATCHER.assertMatch(actual.getMenu(), MENU);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(99999, 101));
    }

//    @Test
//    void update() {
//        Vote updated = UPDATE_REST2_ADDRESS;
//        service.update(new Vote(updated));
//        assertEquals(service.get(REST2.getId()), updated);
//    }

    @Test
    public void delete() {
        service.delete(10000, 101);
        Assertions.assertNull(repository.get(10000, 101));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999, 102));
    }
}