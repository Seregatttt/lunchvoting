package ru.javawebinar.lunchvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javawebinar.lunchvoting.HasId;
import ru.javawebinar.lunchvoting.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "menus")
public class Menu implements HasId {
    public static final int START_SEQ = 10000;

    @Id
    @SequenceGenerator(name = "global_seq_menus", sequenceName = "global_seq_menus", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_menus")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "date_menu", nullable = false)
    @NotNull
    private LocalDate dateMenu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
   // @JsonIgnore
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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", dateMenu=" + dateMenu +
                '}';
    }
}