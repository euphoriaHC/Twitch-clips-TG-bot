CREATE TABLE IF NOT EXISTS users (chat_id bigint primary key, name varchar(64) not null unique);

CREATE TABLE IF NOT EXISTS broadcaster (broadcaster_id int primary key, broadcaster_name varchar(64) not null unique);

CREATE TABLE IF NOT EXISTS game (game_id int primary key, game_name varchar(64) not null unique);

CREATE TABLE IF NOT EXISTS users_follow_list (id bigserial primary key , users_id bigint not null references users(chat_id), bc_id int not null references broadcaster(broadcaster_id), unique (users_id, bc_id));

CREATE TABLE IF NOT EXISTS users_black_list (id bigserial primary key , users_id bigint not null references users(chat_id), bc_id int not null references broadcaster(broadcaster_id), unique (users_id, bc_id));

create EXTENSION pg_trgm;