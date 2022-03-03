create type placement_type as enum ('pre-school', 'leisure');

create table placement (
    id             uuid primary key default uuid_generate_v4(),
    child_id       uuid           not null references child (id),
    placement_type placement_type not null,
    started_on     date           not null,
    ended_on       date
);

create unique index single_placement_index on placement (child_id, placement_type) where ended_on is null;
