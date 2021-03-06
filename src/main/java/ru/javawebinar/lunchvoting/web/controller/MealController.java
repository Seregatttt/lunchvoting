package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.lunchvoting.model.Meal;
import ru.javawebinar.lunchvoting.service.MealService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MealController.REST_MENUS_ID_MEALS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_ADMIN_MENUS = "/rest/admin/menus";
    static final String REST_MENUS_ID_MEALS = REST_ADMIN_MENUS + "/{menuId}/meals";

    protected final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@Valid @RequestBody Meal meal, @PathVariable int menuId) {
        log.info("create {}", meal);
        checkNew(meal);
        Meal created = service.createOrUpdate(meal, menuId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_MENUS_ID_MEALS + "/{id}")
                .buildAndExpand(menuId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable int menuId) {
        log.info("getAll");
        return service.getAll(menuId);
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int menuId, @PathVariable int id) {
        log.info("get restId {}  menuId {}", menuId, id);
        return service.get(id, menuId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Meal meal, @PathVariable int menuId, @PathVariable int id) {
        log.info("update {} with menuId={} id={}", meal, menuId, id);
        assureIdConsistent(meal, id);
        service.createOrUpdate(meal, menuId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int menuId, @PathVariable int id) {
        log.info("delete {} {}", menuId, id);
        service.delete(id, menuId);
    }
}