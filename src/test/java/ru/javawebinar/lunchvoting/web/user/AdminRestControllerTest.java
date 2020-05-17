package ru.javawebinar.lunchvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import ru.javawebinar.lunchvoting.UserTestData;
import ru.javawebinar.lunchvoting.UserTestData;
import ru.javawebinar.lunchvoting.model.Role;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.service.UserService;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;
import ru.javawebinar.lunchvoting.web.AbstractControllerTest;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.lunchvoting.TestUtil.readFromJson;
import static ru.javawebinar.lunchvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.lunchvoting.UserTestData.*;
//import static ru.javawebinar.lunchvoting.util.exception.ErrorType.VALIDATION_ERROR;
//import static ru.javawebinar.lunchvoting.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

class AdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_ADMIN_USERS_URL = AdminRestController.REST_ADMIN_USERS + '/';

    public static User ADMIN = new User(100, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN);
    public static User USER = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static User NEW_USER = new User(null, "new_user", "new_user@mail.ru", "new_pass", Role.ROLE_USER);
    public static User NEW_USER_DOUBLE_EMAIL = new User(null, "DuplicateEmail", "user1@mail.ru", "newPass", Role.ROLE_USER);
    public static User UPDATE_USER1_NEW_PASS = new User(101, "User1", "user1@mail.ru", "newPass", Role.ROLE_USER);
    public static User UPDATE_ADMIN_NEW_EMAIL =new User(100, "Admin", "super-admin@mail.ru", "admin", Role.ROLE_ADMIN);
    public static User UPDATE_ADMIN_DOUBLE_EMAIL =new User(100, "Admin", "user1@mail.ru", "password1", Role.ROLE_ADMIN);
    public static final int USER_ID = 101;
    public static final int ADMIN_ID = 100;

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_USERS_URL + ADMIN_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_USERS_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_USERS_URL + "by?email=" + ADMIN.getEmail())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_USERS_URL + USER_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_ADMIN_USERS_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_USERS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_USERS_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = UPDATE_USER1_NEW_PASS;
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_ADMIN_USERS_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(UserTestData.jsonWithPassword(updated, "newPass")))
                .andExpect(status().isNoContent());
        updated.setId(USER.getId());
        USER_MATCHER.assertMatch(userService.get(USER_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = NEW_USER;
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_ADMIN_USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(UserTestData.jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_USERS_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN, USER,USER2));
    }

    @Test
    void createInvalid() throws Exception {
        User expected = new User(null, null, "", "newPass", Role.ROLE_USER, Role.ROLE_ADMIN);
        perform(MockMvcRequestBuilders.post(REST_ADMIN_USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
               // .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        User updated = new User(USER);
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_ADMIN_USERS_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
               // .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User updated = new User(USER);
        updated.setEmail("admin@mail.ru");
        perform(MockMvcRequestBuilders.put(REST_ADMIN_USERS_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(jsonWithPassword(updated, "password1")))
                .andExpect(status().isUnprocessableEntity())
              //  .andExpect(errorType(VALIDATION_ERROR))
              //  .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User expected = NEW_USER_DOUBLE_EMAIL;
        perform(MockMvcRequestBuilders.post(REST_ADMIN_USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(jsonWithPassword(expected, "newPass")))
                .andExpect(status().isUnprocessableEntity())
              //  .andExpect(errorType(VALIDATION_ERROR))
              //  .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL))
                .andDo(print());

    }
}