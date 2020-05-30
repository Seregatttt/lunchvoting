package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.Vote;
import ru.javawebinar.lunchvoting.service.MenuService;
import ru.javawebinar.lunchvoting.service.VoteService;
import ru.javawebinar.lunchvoting.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteUIController.REST_PROFILE_RESTAURANTS_VOTES_UI, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteUIController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_PROFILE_RESTAURANTS_VOTES_UI = "/rest/profile";  //  /restaurants/{menuId}/votes

    protected final VoteService voteService;
    protected final MenuService menuService;

    public VoteUIController(VoteService voteService, MenuService menuService) {
        this.voteService = voteService;
        this.menuService = menuService;
    }

    @GetMapping("/historyLunchVotes")
    public List<Vote> getLunchVotesBetweenInclude(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate endDate) {
        log.info("get all lunchVotes ");
        return voteService.getLunchVotesBetweenInclude(startDate, endDate, SecurityUtil.authUserId());
    }

    @GetMapping("/showMenuAndRestaurant")
    public List<Menu> getSelectRestaurant(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate endDate) {
        log.info("get selectRestaurant ");
        List<Menu> menus = menuService.getBetweenInclude(startDate, endDate);
        return menus;
    }
}