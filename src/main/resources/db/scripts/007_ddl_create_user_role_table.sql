create table user_role
(
    role_id   int references roles (id),
    user_id int references users (id)
);