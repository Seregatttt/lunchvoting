package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.lunchvoting.model.Restaurant;

import java.util.List;

@Repository
public class RestRepository {
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "id");

    private final CrudRestRepository crudRepository;

    public RestRepository(CrudRestRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Restaurant save(Restaurant rest) {
        return crudRepository.save(rest);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    public Restaurant get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public List<Restaurant> getAll() {
        return crudRepository.findAll(SORT);
    }

    //  @Override
    //  public User getWithMeals(int id) {
    //      return crudRepository.getWithMeals(id);
    //  }
}
