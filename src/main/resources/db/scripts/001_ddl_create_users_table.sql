create table users
(
    id       serial primary key,
    username text NOT NULL unique,
    email    text NOT NULL unique,
    password text NOT NULL
);
