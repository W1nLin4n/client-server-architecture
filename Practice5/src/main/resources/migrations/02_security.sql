CREATE TABLE user(
    id INTEGER PRIMARY KEY,
    username VARCHAR(100),
    password_hash VARCHAR(64),
    access_level VARCHAR(20),
    UNIQUE (username)
);

INSERT INTO user(
    username,
    password_hash,
    access_level
)
VALUES (
    'admin',
    'FB001DFCFFD1C899F3297871406242F097AECF1A5342CCF3EBCD116146188E4B',
    'ADMIN'
)