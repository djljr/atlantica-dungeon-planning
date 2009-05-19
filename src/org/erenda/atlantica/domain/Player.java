package org.erenda.atlantica.domain;

public class Player
{
	long id;
	String name;
	Guild guild;
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Guild getGuild()
	{
		return guild;
	}
	public void setGuild(Guild guild)
	{
		this.guild = guild;
	}
	
	
}
