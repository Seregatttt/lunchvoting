package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.lunchvoting.model.User;

import java.util.List;

@Repository
public class UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudRepository;

    public UserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public User save(User user) {
        return crudRepository.save(user);
    }

    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    //  public User getWithVotes(int id) {
    //      return crudRepository.getWithVotes(id);
    //  }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }
}
