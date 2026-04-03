CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100),
                       email VARCHAR(100),
                       password VARCHAR(100),
    constraint unique_email unique(email)
);