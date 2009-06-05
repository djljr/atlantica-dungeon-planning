insert into guild (name) values ('Vale');
insert into guild (name) values ('CarpeDiem');
insert into guild (name) values ('Scientia');
insert into guild (name) values ('PrisonersofWar');
insert into guild (name) values ('DoomLords');
insert into guild (name) values ('PHANTASTIQUE');
insert into guild (name) values ('SteelDragons');
insert into guild (name) values ('HeLL');
insert into guild (name) values ('Hyde');
insert into guild (name) values ('TwilightProject');

insert into dungeon (id, overview, num_floors, max_boxes, default_tower_box_bonus, default_leader_box_bonus) values
 ('GHOST_SHIP', 'Ghost Ship Overview Text', 4, 250, 2, 0);
 
insert into dungeon_floor (dungeon_id, floor_num, name, overview, completion_boxes, default_penalty_boxes) values
 ('GHOST_SHIP', 1, 'Ghost Town & Graves', 'Ghost Town Overview Text', 50, 1);
