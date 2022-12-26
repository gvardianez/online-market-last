create table reviews
(
    id          bigserial primary key,
    product_id  bigint references products (id),
    username    varchar(255),
    grade       int,
    description varchar(1024),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);