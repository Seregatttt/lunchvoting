package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.repository.RestRepository;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RestRepository repository;

    public RestService(RestRepository repository) {
        this.repository = repository;
    }

    //@CacheEvict(value = "users", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "user must not be null");
        return repository.save(restaurant);
    }

   // @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    //specially comment for test enabled @CacheEvict(value = "users", allEntries = true)
    public void deleteUseCache(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(int id) {
        log.debug("get id={}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    //@Cacheable("users")
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    //@CacheEvict(value = "users", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "user must not be null");
        repository.save(restaurant);
    }

    public Restaurant getWithMenus(int id) {
        return checkNotFoundWithId(repository.getWithMenus(id), id);
    }

//    @CacheEvict(value = "users", allEntries = true)
//    @Transactional
//    public void update(UserTo userTo) {
//        User user = get(userTo.getId());
//        repository.save(UserUtil.updateFromTo(user, userTo));
//    }
}