package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.DateTimeFactory;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;
import ru.javawebinar.lunchvoting.repository.CrudUserRepository;
import ru.javawebinar.lunchvoting.repository.CrudVoteRepository;
import ru.javawebinar.lunchvoting.service.VoteService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;

import java.time.LocalTime;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.lunchvoting.service.VoteServiceTest.TIME_LIMIT_VOTE;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteService service;




    @Test
    void createWithLocation() throws Exception {
       // Vote newCreate = NEW_VOTE1;
        when(timeFactory.getCurrentTime()).thenReturn(LOCAL_TIME);
        when(timeFactory.getCurrentDate()).thenReturn(LOCAL_DATE);
        when(timeFactory.getTimeLimit()).thenReturn(TIME_LIMIT_VOTE);
      //  LocalTime currentTime = dateTimeFactory.getCurrentTime();
        Vote createdVote = new Vote();
        ResultActions action = perform(MockMvcRequestBuilders.post("/rest/profile/restaurants/10006/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER)))
                .andExpect(status().isCreated())
                .andDo(print());

        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        createdVote.setId(newId);
        createdVote.setDateLunch(created.getDateLunch());
        VOTE_MATCHER.assertMatch(created, createdVote);
        VOTE_MATCHER.assertMatch(service.get(10006, 101), createdVote);
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
