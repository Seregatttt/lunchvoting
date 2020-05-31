package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;

class VoteUIControllerTest extends AbstractControllerTest {

    @Test
    void getLunchVotes() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/profile/historyLunchVotes")
                .param("startDate", "2020-05-01")
                .param("endDate", "2020-05-01")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(VOTE)))
                .andDo(print());
    }

    @Test
    void getLunchVotesAllDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/profile/historyLunchVotes")
                //.param("startDate", "2020-05-01")
                // .param("endDate", "2020-05-01")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(VOTE2, VOTE)))
                .andDo(print());
    }

    @Test
    void getSelectRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get("/rest/profile/showMenuAndRestaurant?startDate=2020-05-02&endDate=2020-05-02")
              //  .param("startDate", "2020-05-02")
             //   .param("endDate", "2020-05-02")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}