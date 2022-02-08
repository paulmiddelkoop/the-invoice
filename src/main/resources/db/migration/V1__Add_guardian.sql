create table guardian(
    id         uuid primary key default uuid_generate_v4(),
    first_name text not null,
    last_name  text not null
);