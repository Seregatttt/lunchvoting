package ru.javawebinar.lunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.repository.MealRepository;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int menuId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, menuId);
    }

    public List<Meal> getAll(int menuId) {
        return repository.getAll(menuId);
    }

    public Meal get(int id, int menuId) {
        return checkNotFoundWithId(repository.get(id, menuId), id);
    }

    public Meal getWithMenu(int id, int menuId) {
        return checkNotFoundWithId(repository.getWithMenu(id, menuId), id);
    }

    public void update(Meal meal, int menuId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(repository.save(meal, menuId), meal.id());
    }

    public void delete(int id, int menuId) {
        checkNotFoundWithId(repository.delete(id, menuId), id);
    }
}