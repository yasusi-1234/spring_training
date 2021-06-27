-- CREATE TABLE if not exists customer(
-- 	id BIGINT NOT NULL PRIMARY KEY,
--     first_name VARCHAR(255) NOT NULL,
--     last_name VARCHAR(255) NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     email VARCHAR(255) NOT NULL,
--     tel VARCHAR(32),
--     zip VARCHAR(255) NOT NULL,
--     address VARCHAR(255) NOT NULL,
-- 	upload_file_id BIGINT
-- );

INSERT INTO customer(
	first_name,last_name,password,email,tel,zip,address
) values('太郎', '山田', 'password', 'yamada@com.jp', '090-xxxx-xxxx', 'xxx-xxxx',
'tokyo minato xxxx-xxx-xx');

INSERT INTO customer(
	first_name,last_name,password,email,tel,zip,address
) values('一郎', '鈴木', 'password', 'ichiro@com.jp', '080-xxxx-xxxx', 'xxx-xxxx',
'oosaka sakai xxxx-xxx-xx');