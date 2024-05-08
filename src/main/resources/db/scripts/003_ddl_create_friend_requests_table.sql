create table friend_requests
(
    id          serial primary key,
    sender_id   int references users (id),
    receiver_id int references users (id),
    status      boolean
);
