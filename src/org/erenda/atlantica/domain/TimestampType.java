package org.erenda.atlantica.domain;

public enum TimestampType
{
	CREATE_TIME {
		@Override public DungeonLevel getLevel() { return DungeonLevel.BEFORE_START; }
	},
	START_TIME {
		@Override public DungeonLevel getLevel() { return DungeonLevel.FIRST_FLOOR; }
	},
	FLOOR_ONE_END_TIME {
		@Override public DungeonLevel getLevel() { return DungeonLevel.SECOND_FLOOR; }
	},
	FLOOR_TWO_END_TIME {
		@Override public DungeonLevel getLevel() { return DungeonLevel.THIRD_FLOOR; }
	},
	FINISH_TIME {
		@Override public DungeonLevel getLevel() { return DungeonLevel.AFTER_END; }
	};
	
	public abstract DungeonLevel getLevel();
}
