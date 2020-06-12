package ru.javawebinar.lunchvoting.web;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.javawebinar.lunchvoting.DateTimeFactory;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;
import ru.javawebinar.lunchvoting.repository.CrudUserRepository;
import ru.javawebinar.lunchvoting.repository.CrudVoteRepository;
import ru.javawebinar.lunchvoting.web.controller.VoteController;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
abstract public class AbstractControllerTest {
    static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }
    @Mock
    protected DateTimeFactory timeFactory;

    @Mock
    protected CrudVoteRepository mockVoteRepository;

    @Mock
    protected CrudUserRepository mockUserRepository;

    @Mock
    protected CrudMenuRepository mockMenuRepository;

    @InjectMocks
    protected VoteController controller;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
              //  .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)

                .apply(springSecurity())
                .build();
    }

    public ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
