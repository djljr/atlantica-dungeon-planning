create table guild ( id integer primary key autoincrement, name text );
create table player (id integer primary key autoincrement, name text, might integer, guild_id integer );
create table dungeonrunstats ( id integer primary key autoincrement, dungeon_key text, dungeon_level text );
create table dungeonrunstats_timestamps ( dungeonrun_id integer, timestamp_type text, timestamp_time integer );
create table dungeonrunstats_players ( dungeonrun_id integer, player_id integer, join_time integer, team_type text, join_level text, box_level text );
create table dungeonrunstats_settings (dungeonrun_id integer, box_total integer, bonus_for_tower integer, box_less_1f integer, box_less_2f integer, box_less_3f integer);

--new data model
create table dungeon (id text, overview text, num_floors integer, max_boxes integer, default_tower_box_bonus integer, default_leader_box_bonus integer);

create table dungeon_floor(dungeon_id text, floor_num integer, name text, overview text, completion_boxes integer, default_penalty_boxes integer);

create table dungeonrun (id integer primary key autoincrement, dungeon_id text, current_floor integer, status text);

create table dungeonrun_player (dungeonrun_id integer, player_id integer, guild_id integer, join_time integer, dungeonrun_team_id integer, join_floor integer, box_penalty_floor integer);

create table dungeonrun_team (dungeonrun_id integer, id integer, type text);

create table dungeonrun_general_settings(dungeonrun_id integer, total_boxes integer, tower_box_bonus integer, leader_box_bonus integer);
create table dungeonrun_floor_settings(dungeonrun_id integer, floor_num integer, penalty_boxes integer);