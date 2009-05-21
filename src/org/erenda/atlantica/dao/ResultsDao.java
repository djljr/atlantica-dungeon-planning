package org.erenda.atlantica.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.erenda.atlantica.domain.DungeonLevel;
import org.erenda.atlantica.domain.TeamType;
import org.erenda.atlantica.manager.DungeonResults.PlayerCounts;
import org.springframework.stereotype.Repository;

@Repository
public class ResultsDao extends BaseDao
{

	public Map<String, PlayerCounts> getPlayerCounts(long runId)
	{
		List<Map<String, Object>> results = 
			getJdbcTemplate().queryForList("select g.name guild_name, box_level, team_type " +
				"from dungeonrunstats_players dsp join player p on p.id = dsp.player_id join guild g on g.id = p.guild_id " +
				"where dungeonrun_id = ?", runId);
		
		Map<String, PlayerCounts> counts = new HashMap<String, PlayerCounts>();
		
		for(Map<String, Object> result : results)
		{
			String guild = (String) result.get("guild_name");
			DungeonLevel boxLevel = DungeonLevel.valueOf((String) result.get("box_level"));
			TeamType teamType = TeamType.valueOf((String) result.get("team_type"));
			
			PlayerCounts pc = counts.get(guild);
			if(pc == null)
			{
				pc = new PlayerCounts();
				pc.setGuild(guild);
				counts.put(guild, pc);
			}
			pc.incrementTotal();
			if(teamType.equals(TeamType.GATE_TOWER))
				pc.incrementTower();
			
			switch(boxLevel)
			{
			case AFTER_END:
			case FINALIZED:
			case THIRD_FLOOR:
				pc.incrementThirdFloor(); break;
			case SECOND_FLOOR:
				pc.incrementSecondFloor(); break;
			case FIRST_FLOOR:
				pc.incrementFirstFloor(); break;
			default: break;
			}
		}
		return counts;
	}
}
