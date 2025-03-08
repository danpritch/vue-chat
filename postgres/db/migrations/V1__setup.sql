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

CREATE TABLE chat.conversations (
	id					SERIAL8						PRIMARY KEY,
	owner_id				BIGINT						NOT NULL
);

ALTER TABLE chat.conversations ADD CONSTRAINT owner_id_fk FOREIGN KEY (owner_id) REFERENCES chat.users(id);

CREATE TABLE chat.participants (
	id					SERIAL8						PRIMARY KEY,
	conversation_id				BIGINT						NOT NULL,
	participant_id				BIGINT						NOT NULL
);

ALTER TABLE chat.participants ADD CONSTRAINT conversation_id_fk FOREIGN KEY (conversation_id) REFERENCES chat.conversations(id);

ALTER TABLE chat.participants ADD CONSTRAINT participant_id_fk FOREIGN KEY (participant_id) REFERENCES chat.users(id);

CREATE OR REPLACE FUNCTION chat.create_conversation(p_owner_id BIGINT, p_participant_ids BIGINT[])
RETURNS BIGINT AS $$
WITH conv AS (
    INSERT INTO chat.conversations (owner_id)
    VALUES (p_owner_id)
    RETURNING id AS conv_id
),
parts AS (
    INSERT INTO chat.participants (conversation_id, participant_id)
    SELECT conv_id, unnest(p_participant_ids)
    FROM conv
)
SELECT conv_id FROM conv;
$$ LANGUAGE SQL;
