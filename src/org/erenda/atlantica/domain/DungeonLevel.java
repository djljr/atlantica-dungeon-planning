package org.erenda.atlantica.domain;

public enum DungeonLevel
{
	FINALIZED {
		@Override public DungeonLevel getNext() { return null; }
		@Override public TimestampType getTimestampType() { return null; }
	},
	AFTER_END {
		@Override public DungeonLevel getNext() { return FINALIZED; }
		@Override public TimestampType getTimestampType() { return null; }
	},
	THIRD_FLOOR {
		@Override public DungeonLevel getNext() { return AFTER_END; }
		@Override public TimestampType getTimestampType() { return TimestampType.FINISH_TIME; }
	},
	SECOND_FLOOR {
		@Override public DungeonLevel getNext() { return THIRD_FLOOR; }
		@Override public TimestampType getTimestampType() { return TimestampType.FLOOR_TWO_END_TIME; }
	},
	FIRST_FLOOR {
		@Override public DungeonLevel getNext() { return SECOND_FLOOR; }
		@Override public TimestampType getTimestampType() { return TimestampType.FLOOR_ONE_END_TIME; }
	},
	BEFORE_START {
		@Override public DungeonLevel getNext() { return FIRST_FLOOR; }
		@Override public TimestampType getTimestampType() { return TimestampType.START_TIME; }
	};
	
	DungeonLevel next;
	TimestampType timestamp;
	
	public abstract DungeonLevel getNext();
	public abstract TimestampType getTimestampType();
}
