package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.service.VoteService;
import ru.javawebinar.lunchvoting.web.SecurityUtil;

import java.net.URI;

import static ru.javawebinar.lunchvoting.web.controller.RestaurantController.REST_ADMIN_RESTAURANTS;

@RestController
@RequestMapping(value = VoteController.REST_PROFILE_RESTAURANTS_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_PROFILE_RESTAURANTS_VOTES = "/rest/profile/restaurants/{menuId}/votes";

    protected final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@PathVariable int menuId) {
        log.info("create menuId {}", menuId);
        Vote created = service.create(menuId, SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ADMIN_RESTAURANTS + "/{id}")//TODO
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public Vote get(@PathVariable int menuId) {
        log.info("get menuId {}", menuId);
        return service.get(menuId, SecurityUtil.authUserId());
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable int menuId) {
        log.info("update menuId={} ", menuId);
        //   service.update(menuId, SecurityUtil.authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int menuId) {
        log.info("delete id={}", menuId);
        service.delete(menuId, SecurityUtil.authUserId());
    }
}