package ru.javawebinar.lunchvoting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.lunchvoting.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "user_id"}, name = "votes_unique_date_user_id_idx")})
@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {
    //  public static final int START_SEQ = 0;

//    @Id
//    @SequenceGenerator(name = "global_seq_votes", sequenceName = "global_seq_votes", allocationSize = 1, initialValue = START_SEQ)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_votes")
//    private Integer id;

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
    @NotNull(groups = View.Persist.class)
    private LocalDate dateLunch;

    @Column(name = "date_reg", nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeReg;

    public Vote() {
    }

    public Vote(Integer id, User user, Menu menu) {
        super(id);
        this.user = user;
        this.menu = menu;
        this.dateLunch = menu.getDateMenu();
        this.dateTimeReg = LocalDateTime.now();
    }

    public Vote(Integer id, User user, Menu menu, LocalDateTime localDateTime) {
        this(id, user, menu);
        this.dateTimeReg = localDateTime;
    }

    public Vote(Vote m) {
        this(m.getId(), m.getUser(), m.getMenu());
    }

    public Integer getId() {
        return id;
    }

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
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", dateLunch=" + dateLunch +
                ", dateTimeReg=" + dateTimeReg +
                '}';
    }
}