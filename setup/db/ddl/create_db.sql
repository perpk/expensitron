create database expensitron;

grant all privileges on database expensitron to kpax;

-- also bad
create table master_data_type (
    id serial primary key,
    type varchar (50)
);

create table master_data_item (
    id serial primary key,
    name varchar (255),
    type_id integer references master_data_type(id)
);

alter table master_data_type add constraint uq_master_data_type_type unique (type);

alter table master_data_item add constraint uq_master_data_item_name unique (name);

grant all privileges on table master_data_type to kpax;

grant all privileges on table master_data_item to kpax;

grant usage, select on sequence master_data_type_id_seq to kpax;

grant usage, select on sequence master_data_item_id_seq to kpax;


