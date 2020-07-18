DROP TABLE IF EXISTS access;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(24) NOT NULL UNIQUE,
    username VARCHAR(24) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    verification VARCHAR(64) NOT NULL UNIQUE,
    dark_theme BOOLEAN NOT NULL DEFAULT true,
    is_admin BOOLEAN NOT NULL DEFAULT false
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS access (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    uid INT(6) UNSIGNED,
    path VARCHAR(64) NOT NULL,
    FOREIGN KEY (uid) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
