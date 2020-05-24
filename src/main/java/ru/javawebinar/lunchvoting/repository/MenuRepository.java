package ru.javawebinar.lunchvoting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunchvoting.model.Menu;

import java.util.List;

@Repository
public class MenuRepository {

    private final CrudMenuRepository crudMenuRepository;
    // private final CrudUserRepository crudMenuRepository;

    public MenuRepository(CrudMenuRepository crudMenuRepository, CrudUserRepository crudUserRepository) {
        this.crudMenuRepository = crudMenuRepository;
        //    this.crudUserRepository = crudUserRepository;
    }

    @Transactional
    public Menu save(Menu menu, int restId) {
        if (!menu.isNew() && get(menu.id(), restId) == null) {
            return null;
        }
        //  Menu.setMenu(crudMenuRepository.getOne(menuId));
        return crudMenuRepository.save(menu);
    }

    public boolean delete(int id, int menuId) {
        return crudMenuRepository.delete(id, menuId) != 0;
    }

//    public Menu get(int id, int menuId) {
//        return crudMenuRepository.findById(id).filter(menu -> menu.getRestaurant().getId() == menuId).orElse(null);
//    }

    public Menu get(int id, int menuId) {
        return crudMenuRepository.findById(id).orElse(null);
    }

    public List<Menu> getAll(int menuId) {
        return crudMenuRepository.getAll(menuId);
    }

    public Menu getWithMeal(int id, int menuId) {
        return crudMenuRepository.getWithMeals(id);
    }
    //    public List<Menu> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
//        return crudMealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
//    }
}
