CREATE TABLE tasks
(
    id bigserial primary key,
    title varchar(255) not null,
    description text,
    user_id bigint
)