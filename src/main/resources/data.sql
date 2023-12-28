INSERT INTO roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, description)
VALUES ('2022-01-05 00:00:00', 1, false, '2022-01-05 00:00:00', 1, 'Admin'),
       ('2022-01-05 00:00:00', 1, false, '2022-01-05 00:00:00', 1, 'Manager'),
       ('2022-01-05 00:00:00', 1, false, '2022-01-05 00:00:00', 1, 'Employee');


insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, enabled,
                  first_name, gender, last_name, user_name, phone,role_id, pass_word)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, true, 'ozzy',      'MALE',  'can', 'ozzy','0321654987',1, '$2a$10$Q7ilQ6Hv11qpU0T7xfMzMeqxoPXkvhTVXxFqg0UL2xvLnhNqB7vba'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, true, 'mike',      'MALE', 'woods', 'mike', '0321654987',2, '$2a$10$Q7ilQ6Hv11qpU0T7xfMzMeqxoPXkvhTVXxFqg0UL2xvLnhNqB7vba'),
        ('2022-01-05 00:00:00', 1, false, '2022-01-05 00:00:00', 1, true, 'Samantha', 'FEMALE','Groves','samantha', '0321654987',  3,'$2a$10$Q7ilQ6Hv11qpU0T7xfMzMeqxoPXkvhTVXxFqg0UL2xvLnhNqB7vba');



INSERT INTO projects(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, project_code, project_name,
                     project_detail, project_status, start_date, end_date, manager_id)
VALUES ('2022-01-05 00:00:00', 2, false, '2022-01-05 00:00:00', 2, 'SP00', 'Spring Core', 'Spring Core Project', 'OPEN', '2022-01-05', '2022-06-12', 2),
       ('2022-01-05 00:00:00', 2, false, '2022-01-05 00:00:00', 2, 'SP01', 'Spring Boot', 'Spring Boot Project', 'IN_PROGRESS', '2022-01-05', '2022-06-12', 2),
       ('2022-01-05 00:00:00', 3, false, '2022-01-05 00:00:00', 3, 'SP02', 'Spring MVC', 'Spring MVC Project', 'IN_PROGRESS', '2022-01-05', '2022-06-12', 3),
       ('2022-01-05 00:00:00', 3, false, '2022-01-05 00:00:00', 3, 'SP03', 'Spring Data', 'Spring Data Project', 'OPEN', '2022-01-05', '2022-06-12', 3);

INSERT INTO tasks(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, task_subject, task_detail, task_status, assigned_date, project_id, assigned_employee_id)
VALUES ('2022-01-05 00:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Dependency Injection', 'Injecting dependencies', 'OPEN', '2022-01-05', 1, 3),
       ('2022-01-05 00:00:00', 2, false, '2022-01-05 00:00:00', 2, '@SpringBootApplication', 'Adding @SpringBootApplication annotation', 'IN_PROGRESS', '2022-01-05', 1, 3),
       ('2022-01-05 00:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Controller', 'Creating controllers', 'COMPLETE', '2022-01-05', 1, 3),
       ('2022-01-05 00:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Entity', 'Creating entities', 'OPEN', '2022-01-05', 1, 3);