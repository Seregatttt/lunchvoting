package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javawebinar.lunchvoting.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menus", uniqueConstraints = {@UniqueConstraint(columnNames = {"rest_id", "date_menu"}, name = "menu_date_idx")})
public class Menu extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "date_menu", nullable = false)
    @NotNull
    private LocalDate dateMenu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")

    private List<Meal> meals;

    public Menu() {
    }

    public Menu(Menu m) {
        this.id = m.getId();
        this.dateMenu = m.getDateMenu();
    }

    public Menu(Integer id, LocalDate dateMenu) {
        this.id = id;
        this.dateMenu = dateMenu;
    }

    public Menu(Integer id, Restaurant rest, LocalDate dateMenu) {
        this.id = id;
        this.restaurant = rest;
        this.dateMenu = dateMenu;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public LocalDate getDateMenu() {
        return dateMenu;
    }

    public void setDateMenu(LocalDate dateMenu) {
        this.dateMenu = dateMenu;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", dateMenu=" + dateMenu +
                '}';
    }
}