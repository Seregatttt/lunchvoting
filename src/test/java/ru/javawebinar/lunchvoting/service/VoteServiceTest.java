package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.lunchvoting.model.*;
import ru.javawebinar.lunchvoting.repository.VoteRepository;
import ru.javawebinar.lunchvoting.util.exception.IllegalRequestDataException;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.lunchvoting.web.TestData.MENU_MATCHER;
import static ru.javawebinar.lunchvoting.web.TestData.VOTE_MATCHER;
import static ru.javawebinar.lunchvoting.repository.VoteRepository.*;

public class VoteServiceTest extends AbstractServiceTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected VoteService service;

    @Autowired
    private VoteRepository repository;

    @Test
    void create() {
        Vote create = AbstractControllerTest.NEW_VOTE;
        Vote created = service.create(10006, 102);
        int newId = created.getId();
        create.setId(newId);
        assertEquals(created, create);
        assertEquals(service.get(10006, 102), create);
    }

    @Test
    void createNotFoundMenu() {
        assertThrows(NotFoundException.class, () -> service.create(777, 102));
    }

    @Test
    void createNotFoundUser() {
        assertThrows(NotFoundException.class, () -> service.create(10006, 999999));
    }

    @Test
    void duplicateDateCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(10001, 101));
    }

    @Test
    void get() {
        Vote vote = service.get(10000, 101);
        assertEquals(vote, AbstractControllerTest.VOTE);
    }

    @Test
    void getWithUserAndMenu() throws Exception {
        Vote actual = service.getWithUserAndMenu(10000, 101);
        VOTE_MATCHER.assertMatch(actual, AbstractControllerTest.VOTE);
        MENU_MATCHER.assertMatch(actual.getMenu(), AbstractControllerTest.MENU);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(99999, 101));
    }

    // update if exist old vote on date
    @Test
    void update() {
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 01);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(10, 00);
        Vote updated = AbstractControllerTest.VOTE_UPDATE;
        service.update(10001, 101);
        Vote afterUpdate = repository.getWithUser(10001, 101);
        updated.setId(afterUpdate.getId());
        log.debug("update vote with  menuId={} and userId={} : afterUpdate = {}", 10001, 101, afterUpdate);
        assertEquals(afterUpdate, updated);
    }

    @Test
    void updateOldVote() {
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 30);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(10, 00);
        assertThrows(IllegalRequestDataException.class, () -> service.update(10001, 101));
    }

    @Test
    void updateAfterCriticalTimeNow() {
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 01);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(13, 00);
        assertThrows(IllegalRequestDataException.class, () -> service.update(10001, 101));
    }

    // update if exist old vote on date
    @Test
    void updateFutureDate() {
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 02, 01);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(14, 00);
        Vote updated = AbstractControllerTest.VOTE_UPDATE;
        service.update(10001, 101);
        Vote afterUpdate = repository.getWithUser(10001, 101);
        updated.setId(afterUpdate.getId());
        log.debug("update vote with  menuId={} and userId={} : afterUpdate = {}", 10001, 101, afterUpdate);
        assertEquals(afterUpdate, updated);
    }

    @Test
    void updateNotFoundVote() {
        assertThrows(NotFoundException.class, () -> service.update(99, 101));
    }

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