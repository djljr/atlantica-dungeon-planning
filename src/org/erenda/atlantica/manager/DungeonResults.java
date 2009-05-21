package org.erenda.atlantica.manager;

import java.util.HashMap;
import java.util.Map;

public class DungeonResults
{
	private Map<String, PlayerCounts> guildCounts = new HashMap<String, PlayerCounts>();
	private int distributedBoxTotal;
	private double boxesPerPlayer;
	
	public double getBoxesPerPlayer()
	{
		return boxesPerPlayer;
	}

	public void setBoxesPerPlayer(double boxesPerPlayer)
	{
		this.boxesPerPlayer = boxesPerPlayer;
	}

	public Map<String, PlayerCounts> getGuildCounts()
	{
		return guildCounts;
	}
	
	public void setGuildCounts(Map<String, PlayerCounts> guildCounts)
	{
		this.guildCounts = guildCounts;
	}
	
	public int getDistributedBoxTotal()
	{
		return distributedBoxTotal;
	}


	public void setDistributedBoxTotal(int distributedBoxTotal)
	{
		this.distributedBoxTotal = distributedBoxTotal;
	}

	
	public static class PlayerCounts
	{
		private String guild;
		
		private int total;
		private int tower;
		private int firstFloor;
		private int secondFloor;
		private int thirdFloor;
		
		private int numBoxes;
		
		public void incrementTotal() { total += 1; }
		public void incrementTower() { tower += 1; }
		public void incrementFirstFloor() { firstFloor += 1; }
		public void incrementSecondFloor() { secondFloor += 1; }
		public void incrementThirdFloor() { thirdFloor += 1; }
		
		
		public String getGuild()
		{
			return guild;
		}
		public void setGuild(String guild)
		{
			this.guild = guild;
		}
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
		public int getNumBoxes()
		{
			return numBoxes;
		}
		public void setNumBoxes(int numBoxes)
		{
			this.numBoxes = numBoxes;
		}
		public int getOnTime()
		{
			return total - firstFloor - secondFloor - thirdFloor;
		}
	}
}
