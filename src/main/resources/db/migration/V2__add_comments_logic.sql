CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    post_id    BIGINT                   NOT NULL,
    user_id    BIGINT                   NOT NULL,
    message    TEXT                     NOT NULL,
    created    TIMESTAMP with time zone NULL     DEFAULT CURRENT_TIMESTAMP,
    updated    TIMESTAMP                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN                  NOT NULL DEFAULT false,
    created_by VARCHAR(50),
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);

INSERT INTO comments(post_id, user_id, message)
VALUES (1, 1, 'This is a comment for first post.'),
       (2, 1, 'This is a comment for second post.');