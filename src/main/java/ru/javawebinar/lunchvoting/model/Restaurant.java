package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.javawebinar.lunchvoting.HasId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@NamedQueries({
//        @NamedQuery(name = Restaurant.DELETE, query = "DELETE FROM Restaurant t WHERE t.id=:id"),
//        @NamedQuery(name = Restaurant.BY_MENU, query = "SELECT DISTINCT t FROM Restaurant t LEFT JOIN FETCH t.menus WHERE t.id=?1"),
//        @NamedQuery(name = Restaurant.ALL_SORTED, query = "SELECT t FROM Restaurant t ORDER BY t.id"),
//})
@Entity
@Table(name = "restaurants")
public class Restaurant implements HasId {
    public static final int START_SEQ = 10;
//    public static final String DELETE = "Restaurant.delete";
//    public static final String BY_MENU = "Restaurant.getByMenu";
//    public static final String ALL_SORTED = "Restaurant.getAllSorted";

    @Id
    @SequenceGenerator(name = "global_seq_rest", sequenceName = "global_seq_rest", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_rest")
    protected Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menus;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant restaurant = (Restaurant) o;
        return id.equals(restaurant.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", menus=" + menus +
                '}';
    }
}