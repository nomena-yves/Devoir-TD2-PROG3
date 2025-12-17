create type dish_enum as enum('START','MAIN','DESSERT');
create table Dish(
    id int not null primary key ,
    name varchar (50) not null,
    Dish_type dish_enum
);

create type ingredient_type as enum('VEGETABLE','ANIMAL','DESSERT','OTHER','DAIRY','MARINE');
create table Ingredient(
    id int not null primary key ,
    name varchar(50) not null,
    price int not null ,
    category ingredient_type,
    id_dish int ,
    constraint fk_dish foreign key (id_dish) references Dish (id)
);