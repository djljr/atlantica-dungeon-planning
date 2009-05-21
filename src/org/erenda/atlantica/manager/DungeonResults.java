package org.erenda.atlantica.manager;

import java.util.HashMap;
import java.util.Map;

public class DungeonResults
{
	private Map<String, PlayerCounts> guildCounts = new HashMap<String, PlayerCounts>();

	public Map<String, PlayerCounts> getGuildCounts()
	{
		return guildCounts;
	}
	
	public static class PlayerCounts
	{
		int total;
		int tower;
		int firstFloor;
		int secondFloor;
		int thirdFloor;
		public int getTotal()
		{
			return total;
		}
		public void setTotal(int total)
		{
			this.total = total;
		}
		public int getTower()
		{
			return tower;
		}
		public void setTower(int tower)
		{
			this.tower = tower;
		}
		public int getFirstFloor()
		{
			return firstFloor;
		}
		public void setFirstFloor(int firstFloor)
		{
			this.firstFloor = firstFloor;
		}
		public int getSecondFloor()
		{
			return secondFloor;
		}
		public void setSecondFloor(int secondFloor)
		{
			this.secondFloor = secondFloor;
		}
		public int getThirdFloor()
		{
			return thirdFloor;
		}
		public void setThirdFloor(int thirdFloor)
		{
			this.thirdFloor = thirdFloor;
		}
	}
}
