package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.DateTimeFactory;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.CrudRestaurantRepository;
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

    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;
    private final DateTimeFactory dateTimeFactory;

    public VoteService(
            CrudVoteRepository crudVoteRepository,
            DateTimeFactory dateTimeFactory,
            CrudUserRepository crudUserRepository,
            CrudRestaurantRepository crudRestaurantRepository) {
        this.crudVoteRepository = crudVoteRepository;
        this.dateTimeFactory = dateTimeFactory;
        this.crudUserRepository = crudUserRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Vote createOrUpdate(Vote vote, int restaurantId, int userId) {
        log.debug("save vote with  restaurantId {} and userId {}", restaurantId, userId);

        LocalTime timeLimit = dateTimeFactory.getTimeLimit();
        LocalTime currentTime = dateTimeFactory.getCurrentTime();
        LocalDate currentDate = dateTimeFactory.getCurrentDate();
        Vote existVote = crudVoteRepository.getByDateLunch(currentDate, userId);

        if (existVote != null) {
            if (currentTime.isAfter(timeLimit)) {
                throw new IllegalRequestDataException("time vote is after timeLimit for update restaurantId " + restaurantId);
            }
            crudVoteRepository.delete(existVote.getId(), userId);
        }

        User user = crudUserRepository.getOne(userId);
        Restaurant restaurant = crudRestaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found restaurant for prepare save vote with restaurantId "
                        + restaurantId));

        vote.setUser(user);
        vote.setRestaurant(restaurant);
        vote.setDateVote(currentDate);
        vote.setDateTimeReg(LocalDateTime.of(currentDate, currentTime));
        return crudVoteRepository.save(vote);
    }

    public List<Vote> getUserVotesBetweenInclude(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return crudVoteRepository.getUserVotesBetweenInclude(DateUtil.atStartOfDayOrMin(startDate),
                DateUtil.atStartOfDayOrMax(endDate), userId);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(crudVoteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId).orElse(null), id);
    }

    public Vote get(int userId, int restaurantId, LocalDate dateVote) {
        log.debug("get  userId {} and restaurantId {} and dateVote{} ", userId, restaurantId, dateVote);
        return checkNotFoundWithId(Optional.ofNullable(crudVoteRepository.get(userId, restaurantId, dateVote))
                .orElse(null), restaurantId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(crudVoteRepository.delete(id, userId) != 0, id);
    }
}