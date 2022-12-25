create table users
(
    id           bigserial primary key,
    username     varchar(36) not null unique,
    password     varchar(80) not null,
    email        varchar(50) unique,
    email_status varchar(50) not null,
    created_at   timestamp default current_timestamp,
    updated_at   timestamp default current_timestamp
);

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table users_roles
(
    user_id    bigint not null references users (id),
    role_id    bigint not null references roles (id),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    primary key (user_id, role_id)
);

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into users (username, password, email, email_status)
values ('bob', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com',
        'MAIL_NOT_CONFIRMED'),
       ('john', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com',
        'MAIL_NOT_CONFIRMED');

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2);