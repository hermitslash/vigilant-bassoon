CREATE TABLE IF NOT EXISTS "general".app_roles (
	role_id int4 NOT NULL,
	role_name varchar(65) NULL,
	CONSTRAINT app_roles_pkey PRIMARY KEY (role_id)
);
CREATE TABLE IF NOT EXISTS "general".app_users (
	user_id int8 NOT NULL,
	account_non_expired bool NOT NULL,
	account_non_locked bool NOT NULL,
	company_name varchar(10) NOT NULL,
	credentials_non_expired bool NOT NULL,
	date_created timestamptz(6) NOT NULL,
	email_address varchar(255) NOT NULL,
	enabled bool NOT NULL,
	first_name varchar(65) NULL,
	last_name varchar(75) NULL,
	last_updated timestamptz(6) NOT NULL,
	"password" varchar(255) NULL,
	phone_number varchar(30) NOT NULL,
	username varchar(255) NULL,
	CONSTRAINT app_users_pkey PRIMARY KEY (user_id),
	CONSTRAINT ukspsnwr241e9k9c8p5xl4k45ih UNIQUE (username),
	CONSTRAINT ukxnqa4d2jaycbs2dvk1p9v8xj UNIQUE (email_address)
);
CREATE TABLE IF NOT EXISTS "general".report_info_data (
	invoice_no varchar(25) NOT NULL,
	date_created timestamp(6) NOT NULL,
	last_updated timestamp(6) NOT NULL,
	report_data jsonb NULL,
	CONSTRAINT report_info_data_pkey PRIMARY KEY (invoice_no)
);
-- "general".user_roles definition

-- Drop table

-- DROP TABLE "general".user_roles;

CREATE TABLE IF NOT EXISTS "general".user_roles (
	user_id int8 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)
);

ALTER TABLE "general".user_roles ADD CONSTRAINT fkaf154i5th4vvgbahf8b8pa688 FOREIGN KEY (user_id) REFERENCES "general".app_users(user_id);
ALTER TABLE "general".user_roles ADD CONSTRAINT fkihg20vygk8qb8lw0s573lqsmq FOREIGN KEY (role_id) REFERENCES "general".app_roles(role_id);

