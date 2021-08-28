alter table great
    add constraint great_pk
        unique (user_id, like_id, type);
