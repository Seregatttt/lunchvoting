package ru.javawebinar.lunchvoting.util;

import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.to.RestaurantTo;

import java.time.LocalDate;

public class RestaurantUtil {

    public static RestaurantTo asTo(Menu menu, Long countVote) {

        RestaurantTo restaurantTo = new RestaurantTo(
                menu.getRestaurant().getId(),
                menu.getRestaurant().getName(),
                menu.getRestaurant().getAddress(),
                menu,
                countVote,
                menu.getDateMenu());
        return restaurantTo;
    }
}
