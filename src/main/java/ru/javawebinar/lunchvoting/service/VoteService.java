package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.VoteRepository;
import ru.javawebinar.lunchvoting.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote create(int menuId, int userId) {
        return repository.save(menuId, userId);
    }

    public List<Vote> getLunchVotesBetweenInclude(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getLunchVotesBetweenInclude(DateUtil.atStartOfDayOrMin(startDate),
                DateUtil.atStartOfDayOrMax(endDate), userId);
    }

    public Vote get(int menuId, int userId) {
        log.debug("get menuId={}", menuId);
        return checkNotFoundWithId(repository.get(menuId, userId), menuId);
    }

    public Vote getWithUserAndMenu(int menuId, int userId) {
        return checkNotFoundWithId(repository.getWithUserAndMenu(menuId, userId), menuId);
    }

    public void update(int menuId, int userId) {
        repository.update(menuId, userId);
    }

    public void delete(int menuId, int userId) {
        checkNotFoundWithId(repository.delete(menuId, userId), menuId);
    }
}