alter table t_task
    add column id_application_user uuid not null references t_application_user (id);

create index idx_task_id_application_user on t_task (id_application_user);