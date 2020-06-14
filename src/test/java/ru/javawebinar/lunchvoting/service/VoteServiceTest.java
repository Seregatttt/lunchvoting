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
import ru.javawebinar.lunchvoting.repository.CrudRestaurantRepository;
import ru.javawebinar.lunchvoting.repository.CrudUserRepository;
import ru.javawebinar.lunchvoting.repository.CrudVoteRepository;
import ru.javawebinar.lunchvoting.util.exception.IllegalRequestDataException;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.javawebinar.lunchvoting.DataForTestUnits.*;

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
    private CrudRestaurantRepository mockRestaurantRepository;

    @InjectMocks
    private VoteService mockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create() {
        Vote create = NEW_VOTE;
        Vote created = service.createOrUpdate(create, REST2.getId(), USER1.getId());
        int newId = created.getId();
        create.setId(newId);
        assertEquals(created, create);
        assertEquals(service.get(10030, USER1.getId()), create);
    }

    @Test
    void testCreateBeforeTimeLimit() {

        Vote newVote = new Vote(null, USER1, REST2);
        newVote.setDateVote(LOCAL_DATE);
        final Integer userId = newVote.getUser().getId();
        final Integer restaurantId = newVote.getRestaurant().getId();

        when(timeFactory.getCurrentTime()).thenReturn(LOCAL_TIME);
        when(timeFactory.getCurrentDate()).thenReturn(LOCAL_DATE);
        when(timeFactory.getTimeLimit()).thenReturn(TIME_LIMIT_VOTE);
        when(mockVoteRepository.getByDateLunch(LOCAL_DATE, userId)).thenReturn(VOTE);
        when(mockUserRepository.getOne(userId)).thenReturn(USER1);
        when(mockRestaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(REST2));
        when(mockVoteRepository.save(newVote)).thenReturn(newVote);

        Vote createdVote = mockService.createOrUpdate(newVote, restaurantId, userId);
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
        when(mockVoteRepository.getByDateLunch(LOCAL_DATE, VOTE_UPDATE.getUser().getId())).thenReturn(VOTE);
        assertThrows(IllegalRequestDataException.class, () ->
                mockService.createOrUpdate(newVote, VOTE_UPDATE.getRestaurant().getId(), VOTE_UPDATE.getUser().getId()));
        verify(mockVoteRepository).getByDateLunch(LOCAL_DATE, VOTE_UPDATE.getUser().getId());
    }

    @Test
    void createNotFoundRestaurant() {
        Vote newVote = new Vote(null, USER1, NEW_REST);
        assertThrows(NotFoundException.class, () -> service.createOrUpdate(newVote, 777, USER1.getId()));
    }

    @Test
    void getById() {
        Vote vote = service.get(VOTE.getId(), USER1.getId());
        assertEquals(vote, VOTE);
    }

    @Test
    void getByDate() {
        Vote vote = service.get(USER1.getId(), REST.getId(), LOCAL_DATE);
        assertEquals(vote, VOTE);
    }

    @Test
    void getUserVotesBetweenInclude() throws Exception {
        List<Vote> actual = service.getUserVotesBetweenInclude(
                LocalDate.of(2020, 5, 1),
                LocalDate.of(2020, 5, 2),
                USER1.getId());
        VOTE_MATCHER.assertMatch(actual, List.of(VOTE2, VOTE));
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(99999, 101));
    }

    @Test
    public void delete() {
        service.delete(VOTE.getId(), USER1.getId());
        Assertions.assertNull(repository.get(VOTE.getId(), USER1.getId()));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999, 102));
    }
}