package ru.javawebinar.lunchvoting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.lunchvoting.model.Menu;
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

    public List<Restaurant> getAll() {
        return crudRepository.findAll(SORT);
    }

    public Restaurant get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public Restaurant getWithMenus(int id) {
        return crudRepository.getWithMenus(id);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }
}
