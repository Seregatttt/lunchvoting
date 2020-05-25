package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javawebinar.lunchvoting.HasId;

import javax.persistence.*;
import javax.validation.constraints.*;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@NamedQueries({
//        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal t WHERE t.id=:id"),
//        //  @NamedQuery(name = Restaurant.BY_MENU, query = "SELECT DISTINCT t FROM Restaurant t LEFT JOIN FETCH t.menus WHERE u.email=?1"),
//        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT t FROM Meal t ORDER BY t.id"),
//})
@Entity
@Table(name = "meals")
public class Meal implements HasId {
    public static final int START_SEQ = 1000;
    //public static final String DELETE = "Menu.delete";
    // public static final String BY_MEALS = "Menu.getByMeals";
    //public static final String ALL_SORTED = "Menu.getAllSorted";

    @Id
    @SequenceGenerator(name = "global_seq_meals", sequenceName = "global_seq_meals", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_meals")
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "price", nullable = false)
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Menu menu;

    public Meal() {
    }

    public Meal(Integer id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}