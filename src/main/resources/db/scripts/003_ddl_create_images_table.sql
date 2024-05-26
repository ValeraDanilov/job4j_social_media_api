create table images
(
    id serial primary key,
    name text,
    imageData bytea,
    post_id int not null references posts(id)
);
