package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.lunchvoting.HasId;
import ru.javawebinar.lunchvoting.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "votes")
public class Vote implements HasId {
    public static final int START_SEQ = 0;

    @Id
    @SequenceGenerator(name = "global_seq_votes", sequenceName = "global_seq_votes", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_votes")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Menu menu;

    @Column(name = "date_menu", nullable = false)
    @NotNull
    private LocalDate dateLunch;

    @Column(name = "date_reg", nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeReg;

    public Vote() {
    }

    public Vote(Vote m) {
        this.id = m.getId();
        this.dateTimeReg = m.getDateTimeReg();
    }

    public Vote(Integer id, User user, Menu menu) {
        this.id = id;
        this.user = user;
        this.menu = menu;
        this.dateLunch = menu.getDateMenu();
        this.dateTimeReg = LocalDateTime.now();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public LocalDate getDateLunch() {
        return dateLunch;
    }

    public void setDateLunch(LocalDate dateLunch) {
        this.dateLunch = dateLunch;
    }

    public LocalDateTime getDateTimeReg() {
        return dateTimeReg;
    }

    public void setDateTimeReg(LocalDateTime dateTimeReg) {
        this.dateTimeReg = dateTimeReg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return id.equals(vote.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", dateLunch=" + dateLunch +
                ", dateTimeReg=" + dateTimeReg +
                '}';
    }
}