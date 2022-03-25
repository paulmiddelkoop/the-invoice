-- John and Jane Doe
insert into guardian values ('aab557e1-6130-44d1-b3d9-998f3e981d11', 'John', 'Doe');
insert into guardian values ('d03a5870-a224-444a-832e-c4390150dfb6', 'Jane', 'Doe');

insert into family
values ('ee7cd400-34fe-47a2-af45-bf09e96ea536', 'aab557e1-6130-44d1-b3d9-998f3e981d11',
        'd03a5870-a224-444a-832e-c4390150dfb6', '19890201-3286', 'john@gmail.com', 'e-invoice', '80');

insert into income
values ('843de92b-1498-4a66-9754-b91f1068adcc', 'ee7cd400-34fe-47a2-af45-bf09e96ea536', true, null, '2022-01-01');

insert into income
values ('22479378-aa40-439e-96e1-61f8871dcb5f', 'ee7cd400-34fe-47a2-af45-bf09e96ea536', false, 40000, '2021-01-01');

insert into child
values ('917a0625-2722-4b6b-a856-0f3fee932c35', 'ee7cd400-34fe-47a2-af45-bf09e96ea536', 'Molly', 'Doe', '2018-02-01');
insert into child
values ('4dc6810d-009f-42d8-b487-e7b45c7129bf', 'ee7cd400-34fe-47a2-af45-bf09e96ea536', 'Susan', 'Doe', '2016-06-01');


-- Paul Middelkoop
insert into guardian values ('262c5ce5-c0ca-4560-a98c-f772d0b3f212', 'Paul', 'Middelkoop');

insert into family
values ('b7b07c6e-8f35-4dcb-a062-150bd8e999b9', '262c5ce5-c0ca-4560-a98c-f772d0b3f212',
       null, '19890201-3286', 'paul@gmail.com', 'email', '81');
