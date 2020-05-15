DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq_users RESTART WITH 100;

INSERT INTO users (name, email, password) VALUES
('Admin', 'admin@mail.ru', 'admin'),
('User1', 'user1@mail.ru', 'user1'),
('User2', 'user2@mail.ru', 'user2')
;

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_ADMIN', 100),
('ROLE_USER', 101),
('ROLE_USER', 102);
