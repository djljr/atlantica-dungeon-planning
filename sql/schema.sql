create table guild ( id integer primary key autoincrement, name text );

create table player (id integer primary key autoincrement, name text, guild_id integer );

create table dungeonrunstats ( id integer primary key autoincrement, dungeon_key text, dungeon_level text );

create table dungeonrunstats_timestamps ( dungeonrun_id integer, timestamp_type text, timestamp_time integer );

create table dungeonrunstats_players ( dungeonrun_id integer, player_id integer, join_time integer, team_type text, join_level text, box_level text );

create table dungeonrunstats_settings (dungeonrun_id integer, box_total integer, bonus_per_tower integer, box_less_1f integer, box_less_2f integer, box_less_3f integer);