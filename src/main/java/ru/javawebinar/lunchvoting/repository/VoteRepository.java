package ru.javawebinar.lunchvoting.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.util.exception.IllegalRequestDataException;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteRepository {
    public static LocalTime TIME_CHANGE_VOTE = LocalTime.of(11, 00);
    public static LocalDate DATE_NOW_FOR_TEST_UPDATE = LocalDate.of(2020, 05, 01);
    public static LocalTime TIME_NOW_FOR_TEST_UPDATE = LocalTime.of(11, 00);

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudMenuRepository crudMenuRepository;

    public VoteRepository(CrudVoteRepository crudRepository,
                          CrudUserRepository crudUserRepository,
                          CrudMenuRepository crudMenuRepository) {
        this.crudVoteRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
        this.crudMenuRepository = crudMenuRepository;
    }

    @Transactional
    public Vote save(int menuId, int userId) {
        log.debug("save vote with  menuId {} and userId {}", menuId, userId);
        User user = crudUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user for prepare save vote with userId " + userId));
        Menu menu = crudMenuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Not found menu for prepare save vote with menuId " + menuId));
        Vote vote = new Vote(null, user, menu);
        return crudVoteRepository.save(vote);
    }

    public List<Vote> getLunchVotesBetweenInclude(LocalDate startDate, LocalDate endDate, int userId) {
        log.debug("getAll vote for userId={}", userId);
        return crudVoteRepository.getLunchVotesBetweenInclude(startDate, endDate, userId);
    }

    public Vote get(int menuId, int userId) {
        log.debug("get vote with  menuId={} and userId={}", menuId, userId);
        return Optional.ofNullable(crudVoteRepository.get(menuId, userId)).orElse(null);
    }

    public Vote getByDateLunch(LocalDate dateLunch, int userId) {
        log.debug("getByDateLunch vote with  dateLunch={} and userId={}", dateLunch, userId);
        return Optional.ofNullable(crudVoteRepository.getByDateLunch(dateLunch, userId)).orElse(null);
    }

    public Vote getWithMenu(int menuId, int userId) {
        log.debug("getWithMenu vote with  menuId={} and userId={}", menuId, userId);
        return crudVoteRepository.getWithMenu(menuId, userId);
    }

    public Vote getWithUser(int menuId, int userId) {
        log.debug("getWithUser vote with  menuId={} and userId={}", menuId, userId);
        return crudVoteRepository.getWithUser(menuId, userId);
    }

    public Vote getWithUserAndMenu(int menuId, int userId) {
        log.debug("getWithUserAndMenu vote with  menuId={} and userId={}", menuId, userId);
        return crudVoteRepository.getWithUserAndMenu(menuId, userId);
    }

    @Transactional
    public void update(int menuId, int userId) {
        log.debug("update vote with  menuId={} and userId={}", menuId, userId);
        Menu newMenu = crudMenuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Not found menu with " + menuId));
        Vote voteOld = Optional.of(getByDateLunch(newMenu.getDateMenu(), userId))
                .orElseThrow(() -> new NotFoundException("Not found vote for update with " + menuId));

        // for testing  // for testing  //  for testing
        LocalDate testLocalDateNow = DATE_NOW_FOR_TEST_UPDATE;// date for testing
        LocalTime testLocalTimeNow = TIME_NOW_FOR_TEST_UPDATE;// time for testing

        // if date vote < date now then error
        if (voteOld.getDateLunch().isBefore(testLocalDateNow)) {
            throw new IllegalRequestDataException("date is Before now() menuId=" + menuId);
        }
        // check time after 11:00
        if (voteOld.getDateLunch().isEqual(testLocalDateNow)) {
            if (testLocalTimeNow.isAfter(TIME_CHANGE_VOTE)) {
                throw new IllegalRequestDataException("time is after 11:00 for update menuId " + menuId);
            }
        }

        // if date vote > date now  or time < 11:00
        crudVoteRepository.deleteByIdUserId(voteOld.getId(), userId);
        save(menuId, userId);
    }

    public boolean delete(int menuId, int userId) {
        log.debug("delete vote with  menuId={} and userId={}", menuId, userId);
        return crudVoteRepository.delete(menuId, userId) != 0;
    }
}
