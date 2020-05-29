package ru.javawebinar.lunchvoting.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteRepository {
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

    public List<Vote> getAll(int userId) {
        log.debug("getAll vote for userId={}", userId);
        return crudVoteRepository.getAll(userId);
    }

    public Vote get(int menuId, int userId) {
        log.debug("get vote with  menuId={} and userId={}", menuId, userId);
        Optional<Vote> vote = Optional.ofNullable(crudVoteRepository.get(menuId, userId));
        return vote.orElse(null);
    }

    public Vote getByDateLunch(LocalDate dateLunch, int userId) {
        log.debug("getByDateLunch vote with  dateLunch={} and userId={}", dateLunch, userId);
        Optional<Vote> vote = Optional.ofNullable(crudVoteRepository.getByDateLunch(dateLunch, userId));
        return vote.orElse(null);
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
        crudVoteRepository.deleteByIdUserId(voteOld.getId(), userId);
        save(menuId, userId);
    }

    public boolean delete(int menuId, int userId) {
        log.debug("delete vote with  menuId={} and userId={}", menuId, userId);
        return crudVoteRepository.delete(menuId, userId) != 0;
    }
}
