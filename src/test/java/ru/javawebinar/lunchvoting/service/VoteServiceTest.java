package ru.javawebinar.lunchvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.lunchvoting.DateTimeFactory;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.*;
import ru.javawebinar.lunchvoting.util.exception.IllegalRequestDataException;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;
import static ru.javawebinar.lunchvoting.service.VoteService.VOTE;

public class VoteServiceTest extends AbstractServiceTest {

    public static final LocalTime TIME_LIMIT_VOTE = LocalTime.of(11, 0);
    @Autowired
    private VoteService service;

    @Autowired
    private CrudVoteRepository repository;

    @Mock
    private DateTimeFactory timeFactory;

    @Mock
    private CrudVoteRepository mockVoteRepository;

    @Mock
    private CrudUserRepository mockUserRepository;

    @Mock
    private CrudMenuRepository mockMenuRepository;

    @InjectMocks
    private VoteService mockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /*@Test
    void createOrUpdate() {
        Vote create = NEW_VOTE;
        Vote created = service.createOrUpdate(create,10006, 102);
        int newId = created.getId();
        create.setId(newId);
        assertEquals(created, create);
        assertEquals(service.get(10006, 102), create);
    }

    @Test
    void testCreateBeforeTimeLimit() {

        Vote newVote = new Vote(null, USER1, MENU2, LocalDateTime.of(LOCAL_DATE,LOCAL_TIME));

        final Integer userId = newVote.getUser().getId();
        final Integer menuId = newVote.getMenu().getId();

        when(timeFactory.getCurrentTime()).thenReturn(LOCAL_TIME);
        when(timeFactory.getCurrentDate()).thenReturn(LOCAL_DATE);
        when(timeFactory.getTimeLimit()).thenReturn(TIME_LIMIT_VOTE);
        when(mockVoteRepository.getByDateLunch(LOCAL_DATE, userId)).thenReturn(VOTE);
        when(mockUserRepository.findById(userId)).thenReturn(java.util.Optional.of(USER1));
        when(mockMenuRepository.findById(menuId)).thenReturn(java.util.Optional.of(MENU));
        when(mockVoteRepository.save(newVote)).thenReturn(newVote);

        Vote createdVote = mockService.createOrUpdate(newVote,menuId, userId);
        VOTE_MATCHER.assertMatch(createdVote, newVote);
        verify(mockVoteRepository).getByDateLunch(LOCAL_DATE, userId);
        verify(mockVoteRepository).save(newVote);
    }

    @Test
    void testCreateAfterTimeLimit() {
        Vote newVote = VOTE_UPDATE;
        when(timeFactory.getCurrentTime()).thenReturn(LocalTime.of(12, 0));
        when(timeFactory.getTimeLimit()).thenReturn(LocalTime.of(11, 0));
        when(timeFactory.getCurrentDate()).thenReturn(LOCAL_DATE);
        when(mockVoteRepository.getByDateLunch(LOCAL_DATE,VOTE_UPDATE.getUser().getId())).thenReturn(VOTE);
        assertThrows(IllegalRequestDataException.class, () ->
                mockService.createOrUpdate(newVote,VOTE_UPDATE.getMenu().getId(),VOTE_UPDATE.getUser().getId()));
        verify(mockVoteRepository).getByDateLunch(LOCAL_DATE,VOTE_UPDATE.getUser().getId());
    }

    @Test
    void createNotFoundMenu() {
        Vote newVote = new Vote(null, USER1, MENU2, LocalDateTime.of(LOCAL_DATE,LOCAL_TIME));
        assertThrows(NotFoundException.class, () -> service.createOrUpdate(newVote,777, 102));
    }

    @Test
    void createNotFoundUser() {
        Vote newVote = new Vote(null, USER1, MENU2, LocalDateTime.of(LOCAL_DATE,LOCAL_TIME));
        assertThrows(NotFoundException.class, () -> service.createOrUpdate(newVote,10006, 999999));
    }

    @Test
    void get() {
        Vote vote = service.get(10000, 101);
        assertEquals(vote, VOTE);
    }

    @Test
    void getWithUserAndMenu() throws Exception {
        Vote actual = service.getWithUserAndMenu(10000, 101);
        VOTE_MATCHER.assertMatch(actual, VOTE);
        MENU_MATCHER.assertMatch(actual.getMenu(), MENU);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(99999, 101));
    }

    @Test
    public void delete() {
        service.delete(10000, 101);
        Assertions.assertNull(repository.get(10000, 101));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999, 102));
    }*/
}