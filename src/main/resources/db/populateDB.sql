DELETE FROM votes;
DELETE FROM meals;
DELETE FROM menus;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq_users RESTART WITH 100;
ALTER SEQUENCE global_seq_rest RESTART WITH 10;
ALTER SEQUENCE global_seq_menus RESTART WITH 10000;
ALTER SEQUENCE global_seq_meals RESTART WITH 1000;

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@mail.ru', 'password'),
       ('User1', 'user1@mail.ru', 'password1'),
       ('User2', 'user2@mail.ru', 'password2'),
       ('User3', 'user3@mail.ru', 'password3')
;

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_ADMIN', 100),
       ('ROLE_USER', 101),
       ('ROLE_USER', 102),
       ('ROLE_USER', 103);

ALTER SEQUENCE global_seq_rest RESTART WITH 10;

INSERT INTO restaurants (name, address)
VALUES ('Celler de Can Roca', 'Spain'),
       ('Noma', 'Copenhagen'),
       ('Sato', 'Mexico');

ALTER SEQUENCE global_seq_menus RESTART WITH 10000;

INSERT INTO menus (rest_id, date_menu)
VALUES (10, '2020-05-01'),--10000
       (11, '2020-05-01'),--10001
       (12, '2020-05-01'),--10002
       (10, '2020-05-02'),--10003
       (11, '2020-05-02'),--10004
       (12, '2020-05-02'),--10005
       (10, '2020-05-03');--10006

ALTER SEQUENCE global_seq_meals RESTART WITH 1000;

INSERT INTO meals (menu_id, name, price)
VALUES (10000, 'Salad', 5.50),
       (10000, 'juice', 4.50),
       (10000, 'soup', 3.05),
       (10001, 'cake', 1.05),
       (10001, 'tea', 3.05),
       (10002, 'coffee', 4.05),
       (10002, 'ice cream', 5.05),
       (10003, 'chocolate', 6.05),
       (10003, 'soup', 7.55),
       (10004, 'Salad', 9.00),
       (10004, 'soup', 10.05),
       (10005, 'fat in chocolate', 55.55);

ALTER SEQUENCE global_seq_votes RESTART WITH 0;

INSERT INTO votes (user_id, menu_id,date_menu,date_reg)
VALUES (101, 10000,'2020-05-01','2020-05-01 09:00:00'),
       (102, 10001,'2020-05-01','2020-05-01 12:00:00'),
       (101, 10002,'2020-05-02','2020-05-01 10:00:00'),
       (102, 10003,'2020-05-02','2020-05-01 22:00:00');
