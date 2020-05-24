package ru.javawebinar.lunchvoting;

import ru.javawebinar.lunchvoting.model.*;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

public class TestData {
    public static TestMatcher<User> USER_MATCHER
            = TestMatcher.usingFieldsComparator(User.class, "password");

    public static TestMatcher<Meal> MEAL_MATCHER
            = TestMatcher.usingFieldsComparator(Meal.class, "menu");

    public static TestMatcher<Menu> MENU_MATCHER
            = TestMatcher.usingFieldsComparator(Menu.class, "restaurant","meals");

    public static TestMatcher<Restaurant> REST_MATCHER
            = TestMatcher.usingFieldsComparator(Restaurant.class, "menus");

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static final User ADMIN = new User(100, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN);
    public static final User USER = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static final User NEW_USER = new User(null, "new_user", "new_user@mail.ru", "new_pass", Role.ROLE_USER);
    public static final User NEW_USER_DOUBLE_EMAIL = new User(null, "DuplicateEmail", "user1@mail.ru", "newPass", Role.ROLE_USER);
    public static final User UPDATE_USER1_NEW_PASS = new User(101, "User1", "user1@mail.ru", "newPass", Role.ROLE_USER);
    public static final User UPDATE_ADMIN_NEW_EMAIL = new User(100, "Admin", "super-admin@mail.ru", "admin", Role.ROLE_ADMIN);
    public static final User UPDATE_ADMIN_DOUBLE_EMAIL = new User(100, "Admin", "user1@mail.ru", "password1", Role.ROLE_ADMIN);
    public static final int USER_ID = 101;
    public static final int ADMIN_ID = 100;
}
