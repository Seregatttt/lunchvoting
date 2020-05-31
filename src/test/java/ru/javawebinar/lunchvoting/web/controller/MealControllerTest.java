package ru.javawebinar.lunchvoting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.service.MealService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;
import ru.javawebinar.lunchvoting.web.DataForTest;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.lunchvoting.web.DataForTest.ADMIN;
import static ru.javawebinar.lunchvoting.web.DataForTest.MEAL_MATCHER;

class MealControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService service;

    @Test
    void createWithLocation() throws Exception {
        Meal newCreate = new Meal(null, "super-tea", 999.05f);
        ResultActions action = perform(MockMvcRequestBuilders.post("/rest/admin/menus/10001/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isCreated())
                .andDo(print());

        Meal created = readFromJson(action, Meal.class);
        int newId = created.getId();
        newCreate.setId(newId);
        MEAL_MATCHER.assertMatch(created, newCreate);
        MEAL_MATCHER.assertMatch(service.get(newId, 10001), newCreate);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNewRestWithId() throws Exception {
        Meal newCreate = new Meal(1233, "super-tea", 999.05f);
        perform(MockMvcRequestBuilders.post("/rest/admin/menus/10001/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newCreate)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("Meal must be new (id=null)")))
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/admin/menus/10001/meals")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(List.of(DataForTest.MEAL3, DataForTest.MEAL4)))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/admin/menus/10001/meals/" + 1003)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(DataForTest.MEAL3))
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/admin/menus/10001/meals/" + 2)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/admin/menus/10001/meals/" + 1003))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/admin/menus/10001/meals/" + 1003)
                .with(userHttpBasic(DataForTest.USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Meal updated = new Meal(1003, "update salad", 555f);
        perform(MockMvcRequestBuilders.put("/rest/admin/menus/10001/meals/" + 1003)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        MEAL_MATCHER.assertMatch(service.get(1003, 10001), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Meal updated = new Meal(1003, "update salad", 555f);
        updated.setPrice(0f);
        perform(MockMvcRequestBuilders.put("/rest/admin/menus/10001/meals/" + 1003)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem("[Field price must be greater than or equal to 0.01]")))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/admin/menus/10004/meals/" + 1010)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(1010, 10004));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/admin/menus/10004/meals/" + 99)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}
