package ru.javawebinar.lunchvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.javawebinar.lunchvoting.model.*;

import javax.annotation.PostConstruct;
import java.time.Month;
import java.util.List;
import java.util.Locale;

import static java.time.LocalDate.of;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
abstract public class AbstractControllerTest {
    public static final Restaurant REST = new Restaurant(10, "Celler de Can Roca", "Spain");
    public static final Restaurant REST1 = new Restaurant(11, "Noma", "Copenhagen");
    public static final Restaurant REST2 = new Restaurant(12, "Sato", "Mexico");
    public static final List<Restaurant> RESTAURANTS = List.of(REST, REST1, REST2);
    public static final Restaurant UPDATE_REST2_ADDRESS = new Restaurant(12, "Sato", "Update city");
    public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 01));
    public static final Menu MENU3 = new Menu(10003, of(2020, Month.MAY, 02));
    public static final Menu MENU6 = new Menu(10006, of(2020, Month.MAY, 03));
    public static final User USER1 = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    public static final Vote VOTE2 = new Vote(2, USER1, MENU3);
    public static final Vote VOTE = new Vote(0, USER1, MENU);
    public static final User USER2 = new User(102, "User2", "user2@mail.ru", "password2", Role.ROLE_USER);
    public static final Vote NEW_VOTE = new Vote(null, USER2, MENU6);
    public static final Menu MENU2 = new Menu(10001, of(2020, Month.MAY, 01));
    public static final Vote VOTE_UPDATE = new Vote(null, USER1, MENU2);
    private static final Locale RU_LOCALE = new Locale("ru");
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    public ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
