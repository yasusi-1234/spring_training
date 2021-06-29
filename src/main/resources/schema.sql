CREATE TABLE if not exists customer(
	id BIGINT auto_increment,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    tel VARCHAR(32),
    zip VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
	upload_file_id BIGINT,
	primary key(id)
);