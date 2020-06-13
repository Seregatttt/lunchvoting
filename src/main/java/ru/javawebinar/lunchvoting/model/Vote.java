package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.lunchvoting.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "user_id"}, name = "user_lunch_idx")})
public class Vote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "date_vote", nullable = false)
    @NotNull(groups = View.Persist.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateVote;

    @Column(name = "date_reg", nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeReg;

    public Vote() {
    }

    public Vote(Vote m) {
        this(m.getId(), m.getUser(), m.getRestaurant(), m.getDateVote());
    }

    public Vote(Integer id, User user, Restaurant restaurant) {
        this(id, user, restaurant, LocalDate.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate dateVote) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.dateVote = dateVote;
        this.dateTimeReg = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDateVote() {
        return dateVote;
    }

    public void setDateVote(LocalDate dateVote) {
        this.dateVote = dateVote;
    }

    public LocalDateTime getDateTimeReg() {
        return dateTimeReg;
    }

    public void setDateTimeReg(LocalDateTime dateTimeReg) {
        this.dateTimeReg = dateTimeReg;
    }

    @Override
    public String toString() {
        return "Vote{" +
                ", dateVote=" + dateVote +
                ", dateTimeReg=" + dateTimeReg +
                ", id=" + id +
                '}';
    }
}