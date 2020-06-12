package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.service.RestaurantService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;

class RestaurantControllerTest extends AbstractControllerTest {
    public static final String REST_ADMIN_RESTAURANTS_URL = RestaurantController.REST_ADMIN_RESTAURANTS + '/';

    @Autowired
    private RestaurantService restService;

    @Test
    void createWithLocation() throws Exception {
        Restaurant newCreate = new Restaurant(null, "new_rest", "new_addr");
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isCreated())
                .andDo(print());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.getId();
        newCreate.setId(newId);
        REST_MATCHER.assertMatch(created, newCreate);
        REST_MATCHER.assertMatch(restService.get(newId), newCreate);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNewRestWithId() throws Exception {
        Restaurant newCreate = new Restaurant(10, "new_rest", "new_addr");
        perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("Restaurant must be new (id=null)")))
                .andDo(print());
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant newCreate = new Restaurant(null, " ", null);
        perform(MockMvcRequestBuilders.post(REST_ADMIN_RESTAURANTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("[Field address must not be blank]")))
                .andExpect(jsonPath("$.details", hasItem("[Field name size must be between 2 and 100]")))
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_MATCHER.contentJson(RESTAURANTS))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + REST.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_MATCHER.contentJson(REST))
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_RESTAURANTS_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = UPDATE_REST_NEW_ADDR;
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + 10)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        updated.setId(10);
        REST_MATCHER.assertMatch(restService.get(10), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant updated = UPDATE_REST_NEW_ADDR;
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_ADMIN_RESTAURANTS_URL + 10)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("[Field name size must be between 2 and 100]")))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_RESTAURANTS_URL + 12)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restService.get(12));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_RESTAURANTS_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}