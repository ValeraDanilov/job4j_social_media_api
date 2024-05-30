create table messages
(
    id          serial primary key,
    sender_id   int references users (id),
    receiver_id int references users (id),
    context     text not null,
    created     timestamp
);
