package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Vote;

import java.util.List;

@Repository
public class VoteRepository {
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "id");

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
    public Vote save(Menu menu, int userId) {
//        if (get(menu.id(), userId) == null) {
//            return null;
//        }
        Vote vote = new Vote(null, crudUserRepository.getOne(userId), menu);
        return crudVoteRepository.save(vote);
    }

    public List<Vote> getAll(int userId) {
        return crudVoteRepository.getAll(userId);
    }

    public Vote get(int id, int userId) {
        return crudVoteRepository.findById(id).orElse(null);
    }

    public Vote getWithMenu(int id, int userId) {
        return crudVoteRepository.getWithMenu(id, userId);
    }

    public Vote getWithUser(int id, int userId) {
        return crudVoteRepository.getWithUser(id, userId);
    }

    public Vote getWithUserAndMenu(int id, int userId) {
        return crudVoteRepository.getWithUserAndMenu(id, userId);
    }

//    @Transactional
//    public Vote update(Menu menu, int userId) {
//        if (get(menu.id(), userId) == null) {
//            return null;
//        }
//        Vote vote = new Vote(null, crudUserRepository.getOne(userId), menu);
//        return crudVoteRepository.save(vote);
//    }

    public boolean delete(int id, int userId) {
        return crudVoteRepository.delete(id, userId) != 0;
    }
}
