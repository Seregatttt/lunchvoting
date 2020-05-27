package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.lunchvoting.model.Vote;

import java.util.List;

@Repository
public class VoteRepository {
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "id");

    private final CrudVoteRepository crudRepository;

    public VoteRepository(CrudVoteRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Vote save(Vote vote, int userId) {
        return crudRepository.save(vote);
    }

    public List<Vote> getAll(int userId) {
        return crudRepository.findAll(SORT);
    }

    public Vote get(int id, int userId) {
        return crudRepository.findById(id).orElse(null);
    }

    public Vote getWithMenu(int id, int userId) {
        return crudRepository.getWithMenu(id, userId);
    }

    public Vote getWithUser(int id, int userId) {
        return crudRepository.getWithUser(id, userId);
    }

    public Vote getWithUserAndMenu(int id, int userId) {
        return crudRepository.getWithUserAndMenu(id, userId);
    }

    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }
}
