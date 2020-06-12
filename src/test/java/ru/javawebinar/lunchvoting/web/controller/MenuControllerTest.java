package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;
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
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;

class MenuControllerTest extends AbstractControllerTest {
    public static final String REST_ADMIN_RESTAURANTS_URL = RestaurantController.REST_ADMIN_RESTAURANTS + '/';

    @Autowired
    private MenuService service;

    @Test
    void createWithLocation() throws Exception {
        Menu newCreate = new Menu(null, of(2020, Month.MAY, 30));
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isCreated())
                .andDo(print());

        Menu created = readFromJson(action, Menu.class);
        int newId = created.getId();
        newCreate.setId(newId);
        MENU_MATCHER.assertMatch(created, newCreate);
        MENU_MATCHER.assertMatch(service.get(newId, REST_ID_MENU), newCreate);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNewRestWithId() throws Exception {
        Menu newCreate = new Menu(123, of(2020, Month.MAY, 30));
        perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus")
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
        Menu expected = new Menu(null, of(2020, Month.MAY, 1));
        perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus")
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
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(MENU, MENU3, MENU6)))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus/" + MENU_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(MENU))
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus/" + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus/" + MENU_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus/" + MENU_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(MENU2_ID, of(2020, Month.MAY, 11));
        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + REST2_ID_MENU + "/menus/" + MENU2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        updated.setId(MENU2_ID);
        MENU_MATCHER.assertMatch(service.get(MENU2_ID, REST2_ID_MENU), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Menu updated = new Menu(10002, of(2020, Month.MAY, 11));
        updated.setDateMenu(null);
        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + REST2_ID_MENU + "/menus/" + MENU2_ID)
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
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus/" + MENU3_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(MENU3_ID, REST_ID_MENU));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_RESTAURANTS_URL + REST_ID_MENU + "/menus/" + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}
