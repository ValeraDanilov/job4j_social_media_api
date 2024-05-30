create table images
(
    id serial primary key,
    name text,
    img bytea,
    post_id int not null references posts(id)
);
