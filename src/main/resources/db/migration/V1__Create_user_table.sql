create table if not exists myhub.user
(
    id         int auto_increment
        primary key,
    account_id varchar(50)  null,
    token      char(36)     null,
    gmt_create bigint       null,
    gmt_modify bigint       null,
    name       varchar(50)  null,
    bio        varchar(256) null,
    constraint user_token_uindex
        unique (token)
)ENGINE INNODB default charset = UTF8MB4;

