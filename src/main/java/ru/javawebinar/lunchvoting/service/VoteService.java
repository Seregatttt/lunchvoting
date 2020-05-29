package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.VoteRepository;

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
        // Assert.notNull(menu, "menu must not be null");
        return repository.save(menuId, userId);
    }

    public List<Vote> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Vote get(int menuId, int userId) {
        log.debug("get meniId={}", menuId);
        return checkNotFoundWithId(repository.get(menuId, userId), menuId);
    }

    public Vote getByDateLunch(LocalDate dateLunch, int userId) {
        log.debug("get dateLunch={}", dateLunch);
        return repository.getByDateLunch(dateLunch, userId);
    }


    public Vote getWithMenu(int id, int userId) {
        return checkNotFoundWithId(repository.getWithMenu(id, userId), id);
    }

    public Vote getWithUser(int id, int userId) {
        return checkNotFoundWithId(repository.getWithUser(id, userId), id);
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