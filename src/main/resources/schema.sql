DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS task_category CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;


CREATE TABLE app_user
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

commit;

CREATE TABLE user_roles
(
    user_id INT NOT NULL,
    role    VARCHAR(100) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES app_user (id)
);

commit;

CREATE TABLE task_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

commit;


CREATE TABLE task
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    deadline    TIMESTAMP    NOT NULL, --maybe add a default here
    category_id INT          NOT NULL,
    app_user_id INT          NOT NULL,

    FOREIGN KEY (category_id) REFERENCES task_category (id),
    FOREIGN KEY (app_user_id) REFERENCES app_user (id)
);

commit;