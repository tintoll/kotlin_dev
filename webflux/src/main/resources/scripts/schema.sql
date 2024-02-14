DROP TABLE IF EXISTS boke;
CREATE TABLE boke
(
    id    bigint NOT NULL AUTO_INCREMENT,
    name  varchar(50),
    price int,
    primary key (id)
);