package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.CrudRestaurantRepository;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository repository;

    public RestaurantService(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return repository.findAll(SORT);
    }

    public Restaurant get(int id) {
        log.debug("get id={}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        repository.save(restaurant);
    }

    public Restaurant getWithMenus(int id) {
        return checkNotFoundWithId(repository.getWithMenus(id), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }
}