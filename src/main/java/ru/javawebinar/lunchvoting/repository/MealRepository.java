package ru.javawebinar.lunchvoting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Meal;

import java.util.List;

@Repository
public class MealRepository {

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

    public Meal get(int id, int menuId) {
        return crudMealRepository.findById(id).filter(meal -> meal.getMenu().getId() == menuId).orElse(null);
    }

    public List<Meal> getAll(int menuId) {
        return crudMealRepository.getAll(menuId);
    }

    public Meal getWithMenu(int id, int menuId) {
        return crudMealRepository.getWithMenu(id, menuId);
    }

    public boolean delete(int id, int menuId) {
        return crudMealRepository.delete(id, menuId) != 0;
    }
    //    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
//        return crudMealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
//    }
}
