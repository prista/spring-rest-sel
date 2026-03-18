insert into t_application_user(id, c_username, c_password)
values ('cc607e92-d9af-44d5-84f2-456b7865ed47', 'user1', '{noop}password1'),
       ('0b91ce51-4d2c-4cb6-8fee-b39753d60537', 'user2', '{noop}password2');

insert into t_task(id, c_details, c_completed, id_application_user)
values ('b2715f0d-8459-4256-a8bc-078ceb6f04fd', 'Первая задача', false, 'cc607e92-d9af-44d5-84f2-456b7865ed47'),
         ('e2ac4591-51a0-424d-94a3-0b930201589f', 'Вторая задача', true, 'cc607e92-d9af-44d5-84f2-456b7865ed47'),
         ('bb21b4c1-c9cf-4e24-9283-f2bb2dcbf9c8', 'Третья задача', false, '0b91ce51-4d2c-4cb6-8fee-b39753d60537');