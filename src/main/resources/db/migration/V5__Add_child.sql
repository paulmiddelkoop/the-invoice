create table child (
    id         uuid primary key default uuid_generate_v4(),
    parents_id uuid not null references family (id),
    first_name text not null,
    last_name  text not null,
    born_on    date not null
);