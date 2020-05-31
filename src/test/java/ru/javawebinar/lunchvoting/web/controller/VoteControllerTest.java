package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.*;
import ru.javawebinar.lunchvoting.service.VoteService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;
import ru.javawebinar.lunchvoting.web.DataForTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static java.time.LocalDate.of;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.lunchvoting.web.DataForTest.*;
import static ru.javawebinar.lunchvoting.web.DataForTest.VOTE_MATCHER;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.lunchvoting.repository.VoteRepository.*;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteService service;

    @Test
    void createWithLocation() throws Exception {
        Vote newCreate = NEW_VOTE;
        ResultActions action = perform(MockMvcRequestBuilders.post("/rest/profile/restaurants/10006/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER)))
                .andExpect(status().isCreated())
                .andDo(print());

        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        newCreate.setId(newId);
        VOTE_MATCHER.assertMatch(created, newCreate);
        VOTE_MATCHER.assertMatch(service.get(10006, 101), newCreate);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicateUserDateLunch() throws Exception {
        perform(MockMvcRequestBuilders.post("/rest/profile/restaurants/10001/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER)))
                //.content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is("CONSTRAINS_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("exception user lunch duplicate date")))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/profile/restaurants/10002/votes")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE2))
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/profile/restaurants/10000/votes")
                .with(userHttpBasic(USER2)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/profile/restaurants/10000/votes"))
                .andExpect(status().isUnauthorized());
    }

    // this test is change vote in 10:00
    @Test
    void update() throws Exception {
        // prepare for testing
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 01);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(10, 00);

        perform(MockMvcRequestBuilders.put("/rest/profile/restaurants/10001/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    // this test is change old vote in last time
    @Test
    void updateOldVote() throws Exception {
        // prepare for testing  change vote
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 30);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(10, 00);

        perform(MockMvcRequestBuilders.put("/rest/profile/restaurants/10001/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("date is Before now() menuId=10001")))
                .andDo(print());
    }

    // this test is change old vote in 13:00 time
    @Test
    void updateAfterCriticalTimeNow() throws Exception {
        // prepare for testing  change vote
        TIME_CHANGE_VOTE = LocalTime.of(11, 00);
        DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 01);
        TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(13, 00);

        perform(MockMvcRequestBuilders.put("/rest/profile/restaurants/10001/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("time is after 11:00 for update menuId 10001")))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/profile/restaurants/10003/votes")
                .with(userHttpBasic(USER2)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(10003, 102));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/profile/restaurants/99988888/votes")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void deleteNotUserVote() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/profile/restaurants/10000/votes")
                .with(userHttpBasic(USER2)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}
