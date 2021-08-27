create table myhub.question
(
    id            int auto_increment
        primary key,
    title         varchar(50)   not null,
    creator       int           not null,
    description   text          not null,
    comment_count int default 0 null,
    view_count    int default 0 null,
    like_count    int default 0 null,
    tag           varchar(250)  null,
    gmt_create    bigint        null,
    gmt_modify    bigint        null
)default charset='utf8mb4' engine innodb;
