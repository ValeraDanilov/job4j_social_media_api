create table friends_list
(
    id        serial primary key,
    user_id   int references users (id),
    friend_id int references users (id)
);
