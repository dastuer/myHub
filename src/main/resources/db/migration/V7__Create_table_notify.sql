create table notify
(
    id bigint auto_increment,
    notifier int not null,
    receiver int not null,
    outerId bigint not null,
    type smallint not null,
    gmt_create bigint not null,
    status smallint default 0 not null,
    constraint notify_pk
        primary key (id)
);
