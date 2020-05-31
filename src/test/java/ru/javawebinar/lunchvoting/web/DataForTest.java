package ru.javawebinar.lunchvoting.web;

import ru.javawebinar.lunchvoting.TestMatcher;
import ru.javawebinar.lunchvoting.model.*;
import ru.javawebinar.lunchvoting.web.json.JsonUtil;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;

public class DataForTest {
    public static final Restaurant REST = new Restaurant(10, "Celler de Can Roca", "Spain");
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    public static final List<Restaurant> RESTAURANTS = List.of(REST, REST1, REST2);
    public static final Restaurant UPDATE_REST2_ADDRESS = new Restaurant(12, "Sato", "Update city");
    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User USER1 = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final Vote VOTE = new Vote(0, USER1, MENU);
    public static final Vote VOTE2 = new Vote(2, USER1, MENU3);
    public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static final Vote NEW_VOTE = new Vote(null, USER2, MENU6);
    public static final Menu MENU2 = new Menu(10001, of(2020, Month.MAY, 01));
    public static final Vote VOTE_UPDATE = new Vote(null, USER1, MENU2);
  //  public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
 //   public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User ADMIN = new User(100, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN);
    public static final User USER = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
  //  public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
 //   public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
//    public static final User USER1 = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
//    public static final Vote VOTE2 = new Vote(2, USER1, MENU3);
//    public static final Vote VOTE1 = new Vote(1, USER1, VoteControllerTest.MENU1);
//    public static final Vote VOTE = new Vote(0, USER1, VoteControllerTest.MENU);
//    public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
//    public static final Vote NEW_VOTE = new Vote(null, USER2, MENU6);


    public static TestMatcher<User> USER_MATCHER
            = TestMatcher.usingFieldsComparator(User.class, "password");

    public static TestMatcher<Meal> MEAL_MATCHER
            = TestMatcher.usingFieldsComparator(Meal.class, "menu");

    public static TestMatcher<Menu> MENU_MATCHER
            = TestMatcher.usingFieldsComparator(Menu.class, "restaurant","meals");

    public static TestMatcher<Restaurant> REST_MATCHER
            = TestMatcher.usingFieldsComparator(Restaurant.class, "menus");

    public static TestMatcher<Vote> VOTE_MATCHER
            = TestMatcher.usingFieldsComparator(Vote.class, "menu", "user","dateTimeReg");

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

  //  public static final User ADMIN = new User(100, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN);
  //  public static final User USER = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
 //   public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static final User NEW_USER = new User(null, "new_user", "new_user@mail.ru", "new_pass", Role.ROLE_USER);
    public static final User NEW_USER_DOUBLE_EMAIL = new User(null, "DuplicateEmail", "user1@mail.ru", "newPass", Role.ROLE_USER);
    public static final User UPDATE_USER1_NEW_PASS = new User(101, "User1", "user1@mail.ru", "newPass", Role.ROLE_USER);
    public static final User UPDATE_ADMIN_NEW_EMAIL = new User(100, "Admin", "super-admin@mail.ru", "admin", Role.ROLE_ADMIN);
    public static final User UPDATE_ADMIN_DOUBLE_EMAIL = new User(100, "Admin", "user1@mail.ru", "password1", Role.ROLE_ADMIN);
    public static final int USER_ID = 101;
    public static final int ADMIN_ID = 100;
}
