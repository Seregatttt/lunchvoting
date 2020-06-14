package ru.javawebinar.lunchvoting.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getUsers() {
        return null;
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }
}
