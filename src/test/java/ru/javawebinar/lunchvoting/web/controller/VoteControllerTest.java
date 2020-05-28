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
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import java.time.Month;

import static java.time.LocalDate.of;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.lunchvoting.TestData.VOTE_MATCHER;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;

class VoteControllerTest extends AbstractControllerTest {
    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU1 = new Menu(1001, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User ADMIN = new User(100, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN);
    public static final User USER = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    // public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    //  public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    //public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User USER1 = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static final Vote NEW_VOTE = new Vote(null, USER2, MENU6);
    public static final Vote VOTE = new Vote(0, USER1, MENU);
    public static final Vote VOTE1 = new Vote(1, USER1, MENU1);
    public static final Vote VOTE2 = new Vote(2, USER1, MENU3);


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
                // .andExpect(jsonPath("$.details", hasItem("Menu must be new (id=null)")))
                .andDo(print());
    }

    //    @Test
//    void getAll() throws Exception {
//        perform(MockMvcRequestBuilders.get("/rest/profile/votes" )
//                .with(userHttpBasic(USER)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(VOTE_MATCHER.contentJson(List.of(VOTE2, VOTE)))
//                .andDo(print());
//    }
//
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

    //
//    @Test
//    void update() throws Exception {
//        Menu updated = new Menu(10002, of(2020, Month.MAY, 11));
//        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + "12/menus/" + 10002)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(ADMIN))
//                .content(JsonUtil.writeValue(updated)))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//        updated.setId(10002);
//        MENU_MATCHER.assertMatch(service.get(10002, 12), updated);
//    }
//
//    @Test
//    void updateInvalid() throws Exception {
//        Menu updated = new Menu(10002, of(2020, Month.MAY, 11));
//        updated.setDateMenu(null);
//        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + "12/menus/" + 10002)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(ADMIN))
//                .content(JsonUtil.writeValue(updated)))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
//                .andExpect(jsonPath("$.details", hasItem("[Field dateMenu must not be null]")))
//                .andDo(print());
//    }
//
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
