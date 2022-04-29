INSERT INTO public.os_users (fio, code, is_man, password, telephone_number, time_of_creation, verification)
    VALUES ('ыва', null, true, 'd', '+3', null, true);

insert into os_role(role) values ('ADMIN');
insert into os_role(role) values ('USER');


select * from os_role;
select * from os_type;
select * from os_brand;
select * from os_users;
select * from os_order;
select * from os_device;
select * from os_device_info;
select * from os_order_device;
select * from os_roles_user_entities;

delete from os_roles_user_entities;
delete from os_order_device;
delete from os_role;
delete from os_type;
delete from os_brand;
delete from os_refresh_token;
delete from os_users;
delete from os_order;
delete from os_device;
delete from os_device_info;


drop table os_brand cascade;
drop table os_device cascade;
drop table os_device_info cascade;
drop table os_rating cascade;
drop table os_refresh_token cascade;
drop table os_role cascade;
drop table os_roles_user_entities cascade;
drop table os_type cascade;
drop table os_users cascade;
drop table os_order cascade;
drop table os_order_device cascade;

drop sequence brand_seq;
drop sequence device_info_seq;
drop sequence device_seq;
drop sequence type_seq;