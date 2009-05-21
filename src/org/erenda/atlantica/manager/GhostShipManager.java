package org.erenda.atlantica.manager;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.dao.ResultsDao;
import org.erenda.atlantica.domain.DungeonLevel;
import org.erenda.atlantica.domain.TimestampType;
import org.erenda.atlantica.manager.DungeonResults.PlayerCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GhostShipManager
{
	@Autowired NationDungeonDao nationDungeonDao;
	@Autowired ResultsDao resultsDao;
	
	public DungeonLevel getLevelForRun(long runId)
	{
		List<Map<String, Object>> timestamps = nationDungeonDao.getTimestampsForRun(runId);
		Set<DungeonLevel> completedLevels = EnumSet.noneOf(DungeonLevel.class);
		
		for(Map<String, Object> ts : timestamps)
		{
			completedLevels.add(TimestampType.valueOf((String) ts.get("timestamp_type")).getLevel());
		}
		return DungeonLevel.BEFORE_START;
	}
	
	public DungeonResults getResultsForDungeon(long runId)
	{
		Map<String, Object> settings = nationDungeonDao.getSettings(runId);
		final int _totalBoxes = (Integer) settings.get("box_total");
		final int _bonusForTower = (Integer) settings.get("bonus_for_tower");
		final int _boxLess1F = (Integer) settings.get("box_less_1f");
		final int _boxLess2F = (Integer) settings.get("box_less_2f");
		final int _boxLess3F = (Integer) settings.get("box_less_3f");
		
		int totalPlayerCount = 0;
		int totalBonusBoxes = 0;
		int additionalBoxesDueToLates = 0;
		Map<String, PlayerCounts> result = resultsDao.getPlayerCounts(runId);
		for(Entry<String, PlayerCounts> count : result.entrySet())
		{
			PlayerCounts playerCount = count.getValue();
			
			totalPlayerCount += playerCount.getTotal();
			totalBonusBoxes += _bonusForTower * playerCount.getTower();
			additionalBoxesDueToLates += _boxLess1F * playerCount.getFirstFloor();
			additionalBoxesDueToLates += _boxLess2F * playerCount.getSecondFloor();
			additionalBoxesDueToLates += _boxLess3F * playerCount.getThirdFloor();
		}
		
		int remainingBoxes = _totalBoxes - totalBonusBoxes;
		double boxesPerPerson = ((double)(remainingBoxes + additionalBoxesDueToLates)) / totalPlayerCount;
		int distributedBoxTotal = 0;
		for(Entry<String, PlayerCounts> count : result.entrySet())
		{
			PlayerCounts playerCount = count.getValue();
			double boxes = 0d;
			boxes += playerCount.getTower() * _bonusForTower;
			int totalOnTime = playerCount.getOnTime();
			boxes += totalOnTime * boxesPerPerson;
			boxes += playerCount.getFirstFloor() * (boxesPerPerson - _boxLess1F);
			boxes += playerCount.getSecondFloor() * (boxesPerPerson - _boxLess2F);
			boxes += playerCount.getThirdFloor() * (boxesPerPerson - _boxLess3F);
			
			int distributedBoxes = (int)Math.round(boxes);
			playerCount.setNumBoxes(distributedBoxes);
			distributedBoxTotal += distributedBoxes;
		}
		
		DungeonResults dr = new DungeonResults();
		dr.setDistributedBoxTotal(distributedBoxTotal);
		dr.setGuildCounts(result);
		return dr;
	}
}
