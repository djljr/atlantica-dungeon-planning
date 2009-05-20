package org.erenda.atlantica.domain;

public enum TimestampType
{
	CREATE_TIME(DungeonLevel.BEFORE_START),
	START_TIME(DungeonLevel.FIRST_FLOOR),
	FLOOR_ONE_END_TIME(DungeonLevel.SECOND_FLOOR),
	FLOOR_TWO_END_TIME(DungeonLevel.THIRD_FLOOR),
	FINISH_TIME(DungeonLevel.AFTER_END);
	
	DungeonLevel level;
	private TimestampType(DungeonLevel level)
	{
		this.level = level;
	}
	
	public DungeonLevel getLevel()
	{
		return level;
	}
}
