DELETE
FROM votes;
DELETE
FROM meals;
DELETE
FROM menus;
DELETE
FROM restaurants;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 10000;


INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@mail.ru', 'password'),
       ('User1', 'user1@mail.ru', 'password1'),
       ('User2', 'user2@mail.ru', 'password2'),
       ('User3', 'user3@mail.ru', 'password3')
;

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_ADMIN', 10000),
       ('ROLE_USER', 10001),
       ('ROLE_USER', 10002),
       ('ROLE_USER', 10003);

INSERT INTO restaurants (name, address)
VALUES ('Celler de Can Roca', 'Spain'),--10004
       ('Noma', 'Copenhagen'),--10005
       ('Sato', 'Mexico');--10006

INSERT INTO menus (rest_id, date_menu)
VALUES (10004, '2020-05-01'),--10000 10 10007
       (10005, '2020-05-01'),--10001 11 10008
       (10006, '2020-05-01'),--10002 12 10009
       (10004, '2020-05-02'),--10003 10 10010
       (10005, '2020-05-02'),--10004 11 10011
       (10006, '2020-05-02'),--10005 12 10012
       (10004, '2020-05-03');--10006 10 10013

INSERT INTO meals (menu_id, name, price)
VALUES (10007, 'Salad', 5.50),-- 10014
       (10007, 'juice', 4.50),-- 10015
       (10007, 'soup', 3.05),-- 10016
       (10008, 'cake', 1.05),-- 10017
       (10008, 'tea', 3.05),-- 10018
       (10009, 'coffee', 4.05),-- 10019
       (10009, 'ice cream', 5.05),-- 10020
       (10010, 'chocolate', 6.05),-- 10021
       (10010, 'soup', 7.55),-- 10022
       (10011, 'Salad', 9.00),-- 10023
       (10011, 'soup', 10.05),-- 10024
       (10012, 'fat in chocolate', 55.55);--10025

INSERT INTO votes (user_id, restaurant_id, date_vote, date_reg)
VALUES (10001, 10004, '2020-05-01', '2020-05-01 09:00:00'),
       (10002, 10005, '2020-05-01', '2020-05-01 12:00:00'),
       (10001, 10004, '2020-05-02', '2020-05-01 10:00:00'),
       (10002, 10005, '2020-05-02', '2020-05-01 22:00:00');
