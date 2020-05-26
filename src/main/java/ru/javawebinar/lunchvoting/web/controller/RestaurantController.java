package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.lunchvoting.model.Restaurant;
import ru.javawebinar.lunchvoting.service.RestService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_ADMIN_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_ADMIN_RESTAURANTS = "/rest/admin/restaurants";

    protected final RestService service;

    public RestaurantController(RestService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant rest) {
        log.info("create {}", rest);
        checkNew(rest);
        Restaurant created = service.create(rest);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ADMIN_RESTAURANTS + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant rest, @PathVariable int id) {
        log.info("update {} with id={}", rest, id);
        assureIdConsistent(rest, id);
        service.update(rest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }
//    public void update(UserTo userTo, int id) {
//        log.info("update {} with id={}", userTo, id);
//        assureIdConsistent(userTo, id);
//        //checkModificationAllowed(id);
//        service.update(userTo);
//    }
    //    public User create(UserTo userTo) {
//        log.info("create from to {}", userTo);
//        return create(UserUtil.createNewFromTo(userTo));
//    }

}