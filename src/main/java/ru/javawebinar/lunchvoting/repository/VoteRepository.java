package ru.javawebinar.lunchvoting.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Vote;

import java.util.List;
import java.util.Optional;

@Repository
public class VoteRepository {
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "id");

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
        log.debug("save vote with  meniId={} and userId={}", menuId, userId);
        Vote vote = new Vote(null, crudUserRepository.getOne(userId), crudMenuRepository.getOne(menuId));
        return crudVoteRepository.save(vote);
    }

    public List<Vote> getAll(int userId) {
        log.debug("getAll vote for userId={}", userId);
        return crudVoteRepository.getAll(userId);
    }

    public Vote get(int menuId, int userId) {
        log.debug("get vote with  meniId={} and userId={}", menuId, userId);
        Optional<Vote> vote = Optional.ofNullable(crudVoteRepository.get(menuId, userId));
        return vote.orElse(null);
    }

    public Vote getWithMenu(int menuId, int userId) {
        log.debug("getWithMenu vote with  meniId={} and userId={}", menuId, userId);
        return crudVoteRepository.getWithMenu(menuId, userId);
    }

    public Vote getWithUser(int menuId, int userId) {
        log.debug("getWithUser vote with  meniId={} and userId={}", menuId, userId);
        return crudVoteRepository.getWithUser(menuId, userId);
    }

    public Vote getWithUserAndMenu(int menuId, int userId) {
        log.debug("getWithUserAndMenu vote with  meniId={} and userId={}", menuId, userId);
        return crudVoteRepository.getWithUserAndMenu(menuId, userId);
    }

    public boolean delete(int menuId, int userId) {
        log.debug("delete vote with  meniId={} and userId={}", menuId, userId);
        return crudVoteRepository.delete(menuId, userId) != 0;
    }
}
