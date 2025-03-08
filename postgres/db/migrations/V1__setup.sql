CREATE SCHEMA chat;

ALTER DATABASE postgres SET search_path = "$user", public, chat ;

CREATE TABLE chat.configurations (
	id					SERIAL8						PRIMARY KEY,
	name					VARCHAR						UNIQUE NOT NULL,
	configured				BOOLEAN						NOT NULL
);

CREATE TABLE chat.users (
	id					SERIAL8						PRIMARY KEY,
	name					VARCHAR						UNIQUE NOT NULL
);

