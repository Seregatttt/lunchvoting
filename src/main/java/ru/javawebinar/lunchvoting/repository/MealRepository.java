package ru.javawebinar.lunchvoting.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Meal;

import java.util.List;

@Repository
public class MealRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudMealRepository crudMealRepository;
    private final CrudMenuRepository crudMenuRepository;

    public MealRepository(CrudMealRepository crudMealRepository, CrudMenuRepository crudMenuRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudMenuRepository = crudMenuRepository;
    }

    @Transactional
    public Meal save(Meal meal, int menuId) {
        if (!meal.isNew() && get(meal.id(), menuId) == null) {
            return null;
        }
        meal.setMenu(crudMenuRepository.getOne(menuId));
        return crudMealRepository.save(meal);
    }

    public List<Meal> getAll(int menuId) {
        return crudMealRepository.getAll(menuId);
    }

    public Meal get(int id, int menuId) {
        return crudMealRepository.findById(id).filter(meal -> meal.getMenu().getId() == menuId).orElse(null);
    }

    public Meal getWithMenu(int id, int menuId) {
        log.debug("id={} menuId={}",id,menuId);
        return crudMealRepository.getWithMenu(id);
    }

    public boolean delete(int id, int menuId) {
        return crudMealRepository.delete(id, menuId) != 0;
    }
}
