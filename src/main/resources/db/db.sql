create database mini_dish_db;
       \c mini_dish_db
       create user mini_dish_manager with password 'harena';
               grant usage on schema public to mini_dish_manager;
       grant create on schema public to mini_dish_manager;
                    grant select,insert,update,delete on all tables
    in schema public to mini_dish_manager;
alter default privileges in schema public grant select,insert,update,delete
                                         on tables to mini_dish_manager;