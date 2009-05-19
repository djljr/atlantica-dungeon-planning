package org.erenda.atlantica.manager;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.DungeonLevel;
import org.erenda.atlantica.domain.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GhostShipManager
{
	@Autowired NationDungeonDao nationDungeonDao;
	
	public DungeonLevel getLevelForRun(long runId)
	{
		List<Map<String, Object>> timestamps = nationDungeonDao.getTimestampsForRun(runId);
		Set<DungeonLevel> completedLevels = EnumSet.noneOf(DungeonLevel.class);
		
		for(Map<String, Object> ts : timestamps)
		{
			TimestampType.valueOf((String) ts.get("timestamp_text"));
		}
		return DungeonLevel.BEFORE_START;
	}
}
