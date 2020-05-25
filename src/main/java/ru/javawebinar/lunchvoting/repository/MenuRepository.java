package ru.javawebinar.lunchvoting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MenuRepository {

    private final CrudMenuRepository crudMenuRepository;
    private final CrudRestRepository crudRestRepository;

    public MenuRepository(CrudMenuRepository crudMenuRepository, CrudRestRepository crudRestRepository) {
        this.crudMenuRepository = crudMenuRepository;
        this.crudRestRepository = crudRestRepository;
    }

    @Transactional
    public Menu save(Menu menu, int restId) {
        if (!menu.isNew() && get(menu.id(), restId) == null) {
            return null;
        }
        menu.setRestaurant(crudRestRepository.getOne(restId));
        return crudMenuRepository.save(menu);
    }

    public boolean delete(int id, int menuId) {
        return crudMenuRepository.delete(id, menuId) != 0;
    }

    public Menu get(int id, int restId) {
        return crudMenuRepository.findById(id).filter(m -> m.getRestaurant().getId() == restId).orElse(null);
    }

    public List<Menu> getAll(int menuId) {
        return crudMenuRepository.getAll(menuId);
    }

    public Menu getWithMeals(int id, int menuId) {
        return crudMenuRepository.getWithMeals(id);
    }

    public Menu getWithRest(int id, int restId) {
        return crudMenuRepository.getWithRest(id, restId);
    }

    public Menu getWithRestAndMeals(int id, int restId) {
        return crudMenuRepository.getWithRestAndMeals(id, restId);
    }

    public List<Menu> getBetweenInclude(LocalDate startDateTime, LocalDate endDateTime, int userId) {
        return crudMenuRepository.getBetweenInclude(startDateTime, endDateTime, userId);
    }
}
