package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;
import ru.javawebinar.lunchvoting.repository.CrudRestaurantRepository;
import ru.javawebinar.lunchvoting.repository.CrudVoteRepository;
import ru.javawebinar.lunchvoting.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.lunchvoting.util.RestaurantUtil.asTo;
import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestaurantRepository;
    private final CrudMenuRepository crudMenuRepository;
    private final CrudVoteRepository crudVoteRepository;

    public RestaurantService(CrudRestaurantRepository repository,
                             CrudMenuRepository crudMenuRepository,
                             CrudVoteRepository crudVoteRepository) {
        this.crudRestaurantRepository = repository;
        this.crudMenuRepository = crudMenuRepository;
        this.crudVoteRepository = crudVoteRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return crudRestaurantRepository.save(restaurant);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll(SORT);
    }

    public List<RestaurantTo> getAllWithMenuAndMealsByDate(LocalDate dateMenu) {
        Assert.notNull(dateMenu, "dateMenu must not be null");
        List<Vote> voteList = crudVoteRepository.findAllByDateVote(dateMenu);
        Map<Restaurant, Long> mapCountVote = voteList.stream()
                .collect(Collectors.groupingBy(Vote::getRestaurant, Collectors.counting()));

        List<Menu> menus = crudMenuRepository.getAllByDateWithRestAndMeals(dateMenu);
        List<RestaurantTo> restaurantTos = menus.stream()
                .map(menu -> asTo(menu,mapCountVote.getOrDefault(menu.getRestaurant(),0L)))
                .collect(Collectors.toList());
        return restaurantTos;
    }

    public Restaurant get(int id) {
        log.debug("get id={}", id);
        return checkNotFoundWithId(crudRestaurantRepository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        crudRestaurantRepository.save(restaurant);
    }

    public Restaurant getWithMenus(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.getWithMenus(id), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }
}