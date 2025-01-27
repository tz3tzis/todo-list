insert into app_user (name, password)
values ('john', '$2b$12$mOsP19JXVmRKN65gqHzE9exu94yQCovPYD6IMNtpvrcLaBivwgP2y'), --password: password1
       ('doe', '$2b$12$SW3rVGt9Kvu3FXlzbB9fAetEvIg4zSOf7rRMnV8YGUSPRvBo7Co2i'); --password: password


insert into user_roles (user_id, role)
values ((select id from app_user where name = 'john'), 'USER'),
       ((select id from app_user where name = 'doe'), 'ADMIN');

insert into task_category (name, description)
values ('Personal', 'Tasks related to personal life'),
       ('Work', 'Tasks related to work life'),
       ('Study', 'Tasks related to study life');

insert into task (name, description, deadline, category_id, app_user_id)
values ('Buy groceries', 'Buy groceries for the week', '2025-01-01 14:44:00',
        (select id from task_category where name = 'Personal'), (select id from app_user where name = 'john')),
       ('Finish project', 'Finish project before deadline', '2025-02-01 14:44:00',
        (select id from task_category where name = 'Work'), (select id from app_user where name = 'john')),
       ('Study for exam', 'Study for exam next week', '2025-03-01 14:44:00',
        (select id from task_category where name = 'Study'), (select id from app_user where name = 'doe'));