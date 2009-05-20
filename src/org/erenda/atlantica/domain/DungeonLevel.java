package org.erenda.atlantica.domain;

public enum DungeonLevel implements Comparable<DungeonLevel>
{
	FINALIZED(null, null),
	AFTER_END(null, null),
	THIRD_FLOOR(DungeonLevel.AFTER_END, TimestampType.FINISH_TIME),
	SECOND_FLOOR(DungeonLevel.THIRD_FLOOR, TimestampType.FLOOR_TWO_END_TIME),
	FIRST_FLOOR(DungeonLevel.SECOND_FLOOR, TimestampType.FLOOR_ONE_END_TIME),
	BEFORE_START(DungeonLevel.FIRST_FLOOR, TimestampType.START_TIME);
	
	DungeonLevel next;
	TimestampType timestamp;
	
	private DungeonLevel(DungeonLevel next, TimestampType timestamp)
	{
		this.next = next;
		this.timestamp = timestamp;
	}
	
	public DungeonLevel getNext()
	{
		return next;
	}
	
	public TimestampType getTimestampType()
	{
		return timestamp;
	}
}
