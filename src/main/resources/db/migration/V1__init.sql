CREATE TABLE posts(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    likes INTEGER NOT NULL  DEFAULT 0,
    UNIQUE (title)
);

INSERT INTO posts(title, content, created, likes) VALUES ('First Post', 'This is content for the first post', current_timestamp, 10),
                                                         ('Second Post', 'This is content for the second post', current_timestamp, 3);
