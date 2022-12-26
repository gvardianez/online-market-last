DELETE
from users_roles;

DELETE
from users;

ALTER TABLE users
    ADD subscriber boolean not null;

INSERT into users (username, password, email, email_status, subscriber)
values ('bob', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com',
        'MAIL_NOT_CONFIRMED', 'false'),
       ('john', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com',
        'MAIL_NOT_CONFIRMED', 'false');
