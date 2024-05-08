create table posts
(
    id          serial primary key,
    title       text,
    description text,
    photo       bytea,
    user_id     int NOT NULL references users(id),
    created     timestamp
);
