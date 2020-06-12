package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.DateTimeFactory;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;
import ru.javawebinar.lunchvoting.repository.CrudUserRepository;
import ru.javawebinar.lunchvoting.repository.CrudVoteRepository;
import ru.javawebinar.lunchvoting.util.DateUtil;
import ru.javawebinar.lunchvoting.util.exception.IllegalRequestDataException;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    ///  public static final User USER1 = new User(101, "User1", "user1@mail.ru", "password1", Role.ROLE_USER);
    //  public static final Menu MENU = new Menu(10000, of(2020, Month.MAY, 1));
    //  public static final Vote VOTE = new Vote(0, USER1, MENU);


    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudMenuRepository crudMenuRepository;
    private final DateTimeFactory dateTimeFactory;

    public VoteService(
            CrudVoteRepository crudVoteRepository,
            DateTimeFactory dateTimeFactory,
            CrudUserRepository crudUserRepository,
            CrudMenuRepository crudMenuRepository) {
        this.crudVoteRepository = crudVoteRepository;
        this.dateTimeFactory = dateTimeFactory;
        this.crudUserRepository = crudUserRepository;
        this.crudMenuRepository = crudMenuRepository;
    }

    @Transactional
    public Vote createOrUpdate(Vote vote, int menuId, int userId) {
        log.debug("save vote with  menuId {} and userId {}", menuId, userId);

        LocalTime timeLimit = dateTimeFactory.getTimeLimit();
        LocalTime currentTime = dateTimeFactory.getCurrentTime();
        LocalDate currentDate = dateTimeFactory.getCurrentDate();
        Vote existVote = crudVoteRepository.getByDateLunch(currentDate, userId);

        if (existVote != null) {
            if (currentTime.isAfter(timeLimit)) {
                throw new IllegalRequestDataException("time vote is after timeLimit for update menuId " + menuId);
            }
            crudVoteRepository.delete(existVote.getId(), userId);
        }

        User user = crudUserRepository.getOne(userId);
        Menu menu = crudMenuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Not found menu for prepare save vote with menuId " + menuId));

        vote.setUser(user);
        vote.setMenu(menu);
        vote.setDateLunch(currentDate);
        vote.setDateTimeReg(LocalDateTime.of(currentDate, currentTime));
        return crudVoteRepository.save(vote);
    }

    public List<Vote> getLunchVotesBetweenInclude(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return crudVoteRepository.getLunchVotesBetweenInclude(DateUtil.atStartOfDayOrMin(startDate),
                DateUtil.atStartOfDayOrMax(endDate), userId);
    }

    public Vote get(int menuId, int userId) {
        log.debug("get menuId={}", menuId);
        return checkNotFoundWithId(Optional.ofNullable(crudVoteRepository.get(menuId, userId)).orElse(null), menuId);
    }

   /* public Vote getWithUserAndMenu(int menuId, int userId) {
        return checkNotFoundWithId(Optional.ofNullable(crudVoteRepository.getWithUserAndMenu(menuId, userId)).orElse(null), menuId);
    }*/

    public void delete(int menuId, int userId) {
        checkNotFoundWithId(crudVoteRepository.delete(menuId, userId) != 0, menuId);
    }
}