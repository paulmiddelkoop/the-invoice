create type delivery as enum ('e-invoice', 'email', 'post');

create table family(
    id                       uuid primary key default uuid_generate_v4(),
    guardian1_id             uuid     not null references guardian (id),
    guardian2_id             uuid references guardian (id),
    personal_identity_number text     not null,
    email                    text     not null,
    delivery                 delivery not null,
    customer_number          text     not null,
    ended_on                 date,
    address                  text,
    zip_code                 text,
    city                     text

    constraint address_required check (delivery <> 'post' or
                                       (address is not null and family.zip_code is not null and city is not null))
);

create unique index monogamy_index on family (guardian1_id, guardian2_id) where ended_on is null;