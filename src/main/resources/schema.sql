CREATE TABLE IF NOT EXISTS Course (
    id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    comment VARCHAR(200) NOT NULL,
    version INT,
    PRIMARY KEY(id)
);