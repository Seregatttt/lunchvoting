package ru.javawebinar.lunchvoting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.lunchvoting.model.HasId;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.service.UserService;
import ru.javawebinar.lunchvoting.to.UserTo;
import ru.javawebinar.lunchvoting.util.UserUtil;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        //checkModificationAllowed(id);
        service.update(userTo);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    protected void checkAndValidateForUpdate(HasId user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
    }
}