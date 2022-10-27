DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (dateTime, description, calories, user_id)
VALUES ('2020-10-05 10:00:10', 'Завтрак юзера', 1000, 100000),
       ('2020-10-05 14:00:10', 'Обед юзера', 500, 100000),
       ('2020-10-05 19:00:10', 'Ужин юзера', 700, 100000),
       ('2020-10-06 08:05:10', 'Завтрак юзера', 800, 100000),
       ('2020-10-06 12:01:10', 'Перекус юзера', 700, 100000),
       ('2020-10-05 07:01:10', 'Завтрак админа', 1200, 100001),
       ('2020-10-05 14:05:10', 'Обед админа', 120, 100001);
