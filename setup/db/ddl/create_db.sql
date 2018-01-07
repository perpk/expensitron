create database expensitron;

create table master_data_type (
    id serial primary key,
    type varchar (50)
);

create table master_data_item (
    id serial primary key,
    name varchar (255),
    type_id integer references master_data_type(id)
);