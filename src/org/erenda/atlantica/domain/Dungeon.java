package org.erenda.atlantica.domain;

public enum Dungeon
{
	GHOST_SHIP("Ghost Ship");
	
	private String name;
	private Dungeon(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}