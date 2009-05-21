package org.erenda.atlantica.domain;

public enum Dungeon
{
	GHOST_SHIP("Ghost Ship") {
		@Override
		public String defaultsSql(long id)
		{
			return String.format("insert into dungeonrunstats_settings (dungeonrun_id, box_total, bonus_for_tower, box_less_1f, box_less_2f, box_less_3f) " +
					"values (%s, %s, %s, %s, %s, %s)", id, 290, 2, 1, 3, 5);
		}
	};
	
	private String name;
	private Dungeon(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public abstract String defaultsSql(long id);
}