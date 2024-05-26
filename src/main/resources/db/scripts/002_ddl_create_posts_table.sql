create table posts
(
    id          serial primary key,
    title       text,
    description text,
    user_id     int NOT NULL references users(id),
    created     timestamp
);
