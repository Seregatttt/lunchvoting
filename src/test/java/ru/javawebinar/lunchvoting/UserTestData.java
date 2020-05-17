package ru.javawebinar.lunchvoting;

import ru.javawebinar.lunchvoting.model.Role;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;

//import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static TestMatcher<User> USER_MATCHER
            = TestMatcher.usingFieldsComparator(User.class,  "password");

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static final int USER_ID = 101;
    public static final int ADMIN_ID = 100;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password",  Role.ROLE_USER);
    public static final User ADMIN =
            new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin",  Role.ROLE_ADMIN, Role.ROLE_USER);

//    public static User getNew() {
//        return new User(null, "New", "new@gmail.com", "newPass", 1 false, new Date(), Collections.singleton(Role.USER));
//    }

    public static User getUpdated() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_USER));
        return updated;
    }
}
