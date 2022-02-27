create table income (
    id uuid primary key default uuid_generate_v4(),
    family_id uuid not null references family(id),
    max boolean not null,
    amount int,
    changed_on date not null

    constraint income_check check (max or (amount is not null and amount > 0))
);