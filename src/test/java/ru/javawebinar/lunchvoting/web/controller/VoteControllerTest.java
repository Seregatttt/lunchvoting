package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.service.VoteService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_PROFILE_URL = VoteController.REST_PROFILE + "/";

    @Autowired
    private VoteService voteService;

    @Test
    void createWithLocation() throws Exception {
        Vote createdVote = new Vote();
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_PROFILE_URL + "restaurants/" + REST1.getId() + "/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER)))
                .andExpect(status().isCreated())
                .andDo(print());

        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        createdVote.setId(newId);
        createdVote.setDateVote(created.getDateVote());
        VOTE_MATCHER.assertMatch(created, createdVote);
        VOTE_MATCHER.assertMatch(voteService.get(10030, 10001), createdVote);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_PROFILE_URL + "restaurants/" + REST1.getId() + "/votes/" + 10029)
                .with(userHttpBasic(USER2)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE3))
                .andDo(print());
    }

    @Test
    void getAllRestaurantWithMenuAndMealsByDate() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_PROFILE_URL + "restaurants/meals")
                .param("dateVote", "2020-05-01")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_TO_MATCHER.contentJson(List.of(REST_TO, REST1_TO, REST2_TO)))
                .andDo(print());
    }

    @Test
    void getAllUserVotesBetweenInclude() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_PROFILE_URL + "restaurants/votes")
                .param("startDate", "2020-05-01")
                .param("endDate", "2020-05-02")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(VOTE2, VOTE)))
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_PROFILE_URL + "restaurants/" + REST1.getId() + "/votes/" + 1)
                .with(userHttpBasic(USER2)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_PROFILE_URL + "restaurants/" + REST1.getId() + "/votes/" + 10029))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_PROFILE_URL + "restaurants/" + REST1.getId() + "/votes/" + 10029)
                .with(userHttpBasic(USER2)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(10029, 10002));
    }


    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/profile/restaurants/99988888/votes")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}




