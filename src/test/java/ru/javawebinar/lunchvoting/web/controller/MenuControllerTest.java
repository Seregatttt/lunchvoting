package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.model.Role;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.service.MenuService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.lunchvoting.web.DataForTest.MENU_MATCHER;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;

class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_ADMIN_RESTAURANTS_URL = RestaurantController.REST_ADMIN_RESTAURANTS + '/';

    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User ADMIN = new User(100, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN);
    public static final User USER = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");

    @Autowired
    private MenuService service;

    @Test
    void createWithLocation() throws Exception {
        Menu newCreate = new Menu(null, of(2020, Month.MAY, 30));
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL + "10/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isCreated())
                .andDo(print());

        Menu created = readFromJson(action, Menu.class);
        int newId = created.getId();
        newCreate.setId(newId);
        MENU_MATCHER.assertMatch(created, newCreate);
        MENU_MATCHER.assertMatch(service.get(newId, 10), newCreate);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNewRestWithId() throws Exception {
        Menu newCreate = new Menu(123, of(2020, Month.MAY, 30));
        perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL + "10/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("Menu must be new (id=null)")))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Menu expected = new Menu(null, of(2020, Month.MAY, 01));
        perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL + "10/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is("CONSTRAINS_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("exception menu duplicate date")))
                .andDo(print());

    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + "10/menus")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(MENU, MENU3, MENU6)))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + "10/menus/" + 10000)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(MENU))
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + "10/menus/" + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + "10/menus/" + 10000))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + "10/menus/" + 10000)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(10002, of(2020, Month.MAY, 11));
        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + "12/menus/" + 10002)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        updated.setId(10002);
        MENU_MATCHER.assertMatch(service.get(10002, 12), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Menu updated = new Menu(10002, of(2020, Month.MAY, 11));
        updated.setDateMenu(null);
        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + "12/menus/" + 10002)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("[Field dateMenu must not be null]")))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_RESTAURANTS_URL + "10/menus/" + 10003)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(10003, 10));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_RESTAURANTS_URL + "10/menus/" + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}
