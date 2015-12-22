CREATE TABLE app_cnf_param
    (
        id BIGINT NOT NULL,
        name CHARACTER VARYING(250),
        value CHARACTER VARYING(250),
        PRIMARY KEY (id)
    );

INSERT INTO app_cnf_param (id, name, value) VALUES (1, 'JMS_URL', 't3://localhost:7001');
