create table guild ( id integer primary key autoincrement, name text );

create table player (id integer primary key autoincrement, name text, guild_id integer );

create table dungeonrunstats ( id integer primary key autoincrement, dungeon_key text );

create table dungeonrunstats_timestamps ( dungeonrun_id integer, timestamp_type text, timestamp_time integer );

create table dungeonrunstats_players ( dungeonrun_id integer, player_id integer, join_time integer );