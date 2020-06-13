package ru.javawebinar.lunchvoting.to;

import ru.javawebinar.lunchvoting.model.Menu;

import java.io.Serializable;
import java.time.LocalDate;

public class RestaurantTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String address;

    private Menu menu;

    private Long votesCount;

    private LocalDate date;

    public RestaurantTo() {
    }

    public RestaurantTo(Integer id, String name, String address, Menu menu, Long votesCount, LocalDate date) {
        super(id);
        this.name = name;
        this.address = address;
        this.menu = menu;
        this.votesCount = votesCount;
        this.date = date;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Long votesCount) {
        this.votesCount = votesCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", votesCount=" + votesCount +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}

