package ru.javawebinar.lunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.repository.MealRepository;
import ru.javawebinar.lunchvoting.repository.MenuRepository;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {

    private final MenuRepository repository;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    public Menu get(int id, int restId) {
        return checkNotFoundWithId(repository.get(id, restId), id);
    }

    public void delete(int id, int restId) {
        checkNotFoundWithId(repository.delete(id, restId), id);
    }

    public List<Menu> getAll(int restId) {
        return repository.getAll(restId);
    }

    public void update(Menu menu, int restId) {
        Assert.notNull(menu, "meal must not be null");
        checkNotFoundWithId(repository.save(menu, restId), menu.id());
    }

    public Menu create(Menu menu, int menuId) {
        Assert.notNull(menu, "meal must not be null");
        return repository.save(menu, menuId);
    }

    public Menu getWithMeals(int id, int menuId) {
        return checkNotFoundWithId(repository.getWithMeals(id, menuId), id);
    }

     public Menu getWithRest(int id, int restId) {
        return checkNotFoundWithId(repository.getWithRest(id, restId), id);
    }

    public Menu getWithRestAndMeals(int id, int restId) {
        return checkNotFoundWithId(repository.getWithRestAndMeals(id, restId), id);
    }

}