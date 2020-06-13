package ru.javawebinar.lunchvoting.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.repository.CrudMenuRepository;
import ru.javawebinar.lunchvoting.repository.CrudRestaurantRepository;
import ru.javawebinar.lunchvoting.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {
    private final CrudMenuRepository crudMenuRepository;
    private final CrudRestaurantRepository crudRestRepository;

    public MenuService(CrudMenuRepository crudMenuRepository, CrudRestaurantRepository crudRestRepository) {
        this.crudMenuRepository = crudMenuRepository;
        this.crudRestRepository = crudRestRepository;
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Menu createOrUpdate(Menu menu, int restId) {
        Assert.notNull(menu, "menu must not be null");
        if (!menu.isNew() && get(menu.getId(), restId) == null) {
            return null;
        }
        menu.setRestaurant(crudRestRepository.getOne(restId));
        return crudMenuRepository.save(menu);
    }

    public List<Menu> getAll(int restId) {
        return crudMenuRepository.getAll(restId);
    }

    public Menu get(int id, int restId) {
        return checkNotFoundWithId(crudMenuRepository.findById(id).
                filter(m -> m.getRestaurant().getId() == restId).orElse(null), id);
    }

    public Menu getWithMeals(int id, int restId) {
        return checkNotFoundWithId(crudMenuRepository.getWithMeals(id, restId), id);
    }

    public Menu getWithRest(int id, int restId) {
        return checkNotFoundWithId(crudMenuRepository.getWithRest(id, restId), id);
    }

    public Menu getWithRestAndMeals(int id, int restId) {
        return checkNotFoundWithId(crudMenuRepository.getWithRestAndMeals(id, restId), id);
    }

    public List<Menu> getAllByDateWithRestAndMeals(LocalDate dateMenu) {
        return (crudMenuRepository.getAllByDateWithRestAndMeals(dateMenu));
    }

    public List<Menu> getBetweenInclude(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return crudMenuRepository.getBetweenInclude(DateUtil.atStartOfDayOrMin(startDate), DateUtil.atStartOfDayOrMax(endDate));
    }

    public void delete(int id, int restId) {
        checkNotFoundWithId(crudMenuRepository.delete(id, restId) != 0, id);
    }
}