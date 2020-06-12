package ru.javawebinar.lunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.repository.CrudMealRepository;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final CrudMealRepository crudMealRepository;
    private final CrudMenuRepository crudMenuRepository;

    public MealService(CrudMealRepository repository, CrudMenuRepository crudMenuRepository) {
        this.crudMealRepository = repository;
        this.crudMenuRepository = crudMenuRepository;
    }

    public Meal createOrUpdate(Meal meal, int menuId) {
        Assert.notNull(meal, "meal must not be null");
        if (!meal.isNew() && get(meal.getId(), menuId) == null) {
            return null;
        }
        meal.setMenu(crudMenuRepository.getOne(menuId));
        return crudMealRepository.save(meal);
    }

    public List<Meal> getAll(int menuId) {
        return crudMealRepository.getAll(menuId);
    }

    public Meal get(int id, int menuId) {
        return checkNotFoundWithId(crudMealRepository.findById(id).
                filter(m -> m.getMenu().getId() == menuId).orElse(null), id);
    }

    public Meal getWithMenu(int id, int menuId) {
        return checkNotFoundWithId(crudMealRepository.getWithMenu(id, menuId), id);
    }

    public void delete(int id, int menuId) {
        checkNotFoundWithId(crudMealRepository.delete(id, menuId) != 0, id);
    }
}