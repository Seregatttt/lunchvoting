package ru.javawebinar.lunchvoting.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

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
        // return checkNotFoundWithId(repository.get(id, menuId), id);
        Meal meal = crudMealRepository.findById(id).filter(m -> m.getMenu().getId() == menuId)
                .orElse(null);
        return meal;
           //     .orElseThrow(() -> new NotFoundException("Not found meal with id " + id + " and userId " + menuId));
    }

    public Meal getWithMenu(int id, int menuId) {
        log.debug("id={} menuId={}", id, menuId);
        return crudMealRepository.getWithMenu(id);
    }

    public void delete(int id, int menuId) {
        // checkNotFoundWithId(repository.delete(id, menuId), id);
        checkNotFoundWithId(crudMealRepository.delete(id, menuId) != 0, id);
    }
}
