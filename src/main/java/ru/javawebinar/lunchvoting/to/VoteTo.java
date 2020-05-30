package ru.javawebinar.lunchvoting.to;

import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.lunchvoting.HasId;
import ru.javawebinar.lunchvoting.View;
import ru.javawebinar.lunchvoting.model.Menu;
import ru.javawebinar.lunchvoting.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

public class VoteTo extends BaseTo implements HasId, Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(groups = View.Persist.class)
    private User user;

    @NotNull(groups = View.Persist.class)
    private Menu menu;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeReg;

    public VoteTo() {
    }

    public VoteTo(Integer id, User user,  Menu menu, LocalDateTime dateTimeReg) {
        super(id);
        this.user = user;
        this.menu = menu;
        this.dateTimeReg = dateTimeReg;
    }
}




