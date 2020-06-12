package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.service.MenuService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNew;
import static ru.javawebinar.lunchvoting.web.controller.RestaurantController.REST_ADMIN_RESTAURANTS;

@RestController
@RequestMapping(value = MenuController.REST_ID_RESTAURANTS_MENUS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_ID_RESTAURANTS_MENUS = REST_ADMIN_RESTAURANTS + "/{restId}/menus";

    protected final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable int restId) {
        log.info("create {}", menu);
        checkNew(menu);
        Menu created = service.createOrUpdate(menu, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ADMIN_RESTAURANTS + "/{restId}/menus/{id}")
                .buildAndExpand(restId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<Menu> getAll(@PathVariable int restId) {
        log.info("getAll");
        return service.getAll(restId);
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int restId, @PathVariable int id) {
        log.info("get restId {}  menuId {}", restId, id);
        return service.get(id, restId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int restId, @PathVariable int id) {
        log.info("update {} with restId={} id={}", menu, restId, id);
        assureIdConsistent(menu, id);
        service.createOrUpdate(menu, restId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete {} {}", restId, id);
        service.delete(id, restId);
    }
}