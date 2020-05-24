package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javawebinar.lunchvoting.HasId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@NamedQueries({
//        @NamedQuery(name = Menu.DELETE, query = "DELETE FROM Menu t WHERE t.id=:id"),
//        //  @NamedQuery(name = Restaurant.BY_MENU, query = "SELECT DISTINCT t FROM Restaurant t LEFT JOIN FETCH t.menus WHERE u.email=?1"),
//        @NamedQuery(name = Menu.ALL_SORTED, query = "SELECT t FROM Menu t ORDER BY t.id"),
//})
@Entity
@Table(name = "menus")
public class Menu implements HasId {
    public static final int START_SEQ = 10000;
//    public static final String DELETE = "Menu.delete";
//    public static final String BY_MEALS = "Menu.getByMeals";
//    public static final String ALL_SORTED = "Menu.getAllSorted";

    @Id
    @SequenceGenerator(name = "global_seq_menus", sequenceName = "global_seq_menus", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_menus")
    protected Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    protected Restaurant restaurant;

    @Column(name = "date_menu", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private LocalDate dateMenu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Meal> meals;

    public Menu() {
    }

    public Menu(Integer id, LocalDate dateMenu) {
        this.id = id;
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
                ", restaurant=" + restaurant +
                ", dateMenu=" + dateMenu +
                ", meals=" + meals +
                '}';
    }
}