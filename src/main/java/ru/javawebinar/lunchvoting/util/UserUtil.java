package ru.javawebinar.lunchvoting.util;

import ru.javawebinar.lunchvoting.model.User;
import ru.javawebinar.lunchvoting.to.UserTo;

public class UserUtil {

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
//TODO
//    public static User createNewFromTo(UserTo userTo) {
//        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.ROLE_USER);
//    }
//    public static User updateFromTo(User user, UserTo userTo) {
//        user.setName(userTo.getName());
//        user.setEmail(userTo.getEmail().toLowerCase());
//        user.setPassword(userTo.getPassword());
//        return user;
//    }
//
//    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
//        String password = user.getPassword();
//        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
//        user.setEmail(user.getEmail().toLowerCase());
//        return user;
//    }
}