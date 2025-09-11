CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(80) NOT NULL,
    email VARCHAR(50) UNIQUE,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP,
    registration_status VARCHAR(30) NOT NULL,
    last_login TIMESTAMP,
    deleted BOOLEAN NOT NULL  DEFAULT false
);


CREATE TABLE posts(
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL ,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    likes INTEGER NOT NULL  DEFAULT 0,
    created_by VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (title)
);

INSERT INTO users(username, password, email, created, updated, registration_status, last_login, deleted)
                VALUES ('first_user', 'password', 'first_user@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
                       ('second_user', 'password', 'second_user@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
                       ('third_user', 'password', 'third_user@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false);

INSERT INTO posts(user_id, title, content, created,updated, deleted, likes) VALUES (1, 'First Post', 'This is content for the first post', current_timestamp, current_timestamp, false, 10),
                                                         (1, 'Second Post', 'This is content for the second post', current_timestamp, current_timestamp, false, 3);
