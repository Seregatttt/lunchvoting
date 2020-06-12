package ru.javawebinar.lunchvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.lunchvoting.AuthorizedUser;
import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.repository.CrudUserRepository;
import ru.javawebinar.lunchvoting.to.UserTo;
import ru.javawebinar.lunchvoting.util.UserUtil;

import java.util.List;

import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudUserRepository repository;

    public UserService(CrudUserRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return repository.findAll(SORT_NAME);
    }

    public User get(int id) {
        log.debug("get id={}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        log.debug("get email={}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.getId());
        repository.save(UserUtil.updateFromTo(user, userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public void deleteUseCache(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}