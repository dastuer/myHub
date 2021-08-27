create table comment
(
    id bigint auto_increment,
    parent_id bigint not null,
    commenter int not null,
    type smallint not null,
    gmt_create bigint null,
    gmt_modify bigint null,
    like_count bigint default 0 null,
    content varchar(1024) not null,
    constraint comment_pk
        primary key (id)
);
