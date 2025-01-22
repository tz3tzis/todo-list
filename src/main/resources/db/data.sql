insert into app_user (name, password)
values ('john', 'password1'),
       ('doe', 'password2');


insert into user_roles (user_id, role)
values ((select id from app_user where name = 'john'), 'USER'),
       ((select id from app_user where name = 'doe'), 'ADMIN');

insert into task_category (name, description)
values ('Personal', 'Tasks related to personal life'),
       ('Work', 'Tasks related to work life'),
       ('Study', 'Tasks related to study life');

insert into task (name, description, deadline, category_id, app_user_id)
values ('Buy groceries', 'Buy groceries for the week', '2022-02-06 14:44:00',
        (select id from task_category where name = 'Personal'), (select id from app_user where name = 'john')),
       ('Finish project', 'Finish project before deadline', '2022-02-06 14:44:00',
        (select id from task_category where name = 'Work'), (select id from app_user where name = 'john')),
       ('Study for exam', 'Study for exam next week', '2022-02-06 14:44:00',
        (select id from task_category where name = 'Study'), (select id from app_user where name = 'doe'));