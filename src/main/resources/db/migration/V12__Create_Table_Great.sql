create table great
(
    id bigint auto_increment,
    user_id int not null,
    like_id bigint not null,
    type smallint default 0 not null,
    status smallint default 0 not null,
    constraint great_pk
        primary key (id)
);
