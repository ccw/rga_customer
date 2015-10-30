CREATE TABLE USER (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_name VARCHAR(255) not null,
  password VARCHAR(255) not null,
  salt BIGINT not null,
	roles VARCHAR(255) not null
);

ALTER TABLE USER ADD CONSTRAINT USER_NAME_UNIQUE UNIQUE(user_name);

CREATE TABLE CUSTOMER (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(255) not null,
	last_name VARCHAR(255) not null,
	user_id BIGINT not null
);
