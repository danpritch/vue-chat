CREATE SCHEMA vue_chess;

ALTER DATABASE postgres SET search_path = "$user", public, vue_chess ;

CREATE TABLE vue_chess.configurations (
	id					SERIAL8						PRIMARY KEY,
	name					VARCHAR						UNIQUE NOT NULL,
	configured				BOOLEAN						NOT NULL
);

CREATE TABLE vue_chess.players (
	id					SERIAL8						PRIMARY KEY,
	name					VARCHAR						UNIQUE NOT NULL
);

