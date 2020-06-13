package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.lunchvoting.DateTimeFactory;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.service.RestaurantService;
import ru.javawebinar.lunchvoting.service.VoteService;
import ru.javawebinar.lunchvoting.to.RestaurantTo;
import ru.javawebinar.lunchvoting.web.SecurityUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_PROFILE = "/rest/profile";

    protected final VoteService voteService;
    protected final RestaurantService restaurantService;
    private final DateTimeFactory dateTimeFactory;

    public VoteController(VoteService service,
                          RestaurantService restaurantService,
                          DateTimeFactory dateTimeFactory) {
        this.voteService = service;
        this.restaurantService = restaurantService;
        this.dateTimeFactory = dateTimeFactory;
    }

    @PostMapping(value = "/restaurants/{restaurantId}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@PathVariable int restaurantId) {
        log.info("create menuId {}", restaurantId);
        Vote created = voteService.createOrUpdate(new Vote(), restaurantId, SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_PROFILE)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/restaurants/meals")
    public List<RestaurantTo> getAllWithMenuAndMealsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate dateVote) {
        log.info("get selectRestaurant ");
        return restaurantService.getAllWithMenuAndMealsByDate(dateVote);
    }

    @GetMapping("/restaurants/votes")
    public List<Vote> getAllUserVotesBetweenInclude(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate endDate) {
        log.info("get all userVotes ");
        return voteService.getUserVotesBetweenInclude(startDate, endDate, SecurityUtil.authUserId());
    }

    @GetMapping("/restaurants/{restaurantId}/votes/{id}")
    public Vote getUserVote(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get userVote id {} ", id);
        return voteService.get(id, SecurityUtil.authUserId(), restaurantId);
    }

    @DeleteMapping("/restaurants/{restaurantId}/votes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete  id {} usrId {} restaurantId {}", id, SecurityUtil.authUserId(), restaurantId);
        voteService.delete(id, SecurityUtil.authUserId());
    }
}
