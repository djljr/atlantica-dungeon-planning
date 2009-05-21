package org.erenda.atlantica.dao;

import java.util.List;
import java.util.Map;

import org.erenda.atlantica.domain.TeamType;
import org.erenda.atlantica.manager.DungeonResults.PlayerCounts;
import org.springframework.stereotype.Repository;

@Repository
public class ResultsDao extends BaseDao
{

	public Map<String, PlayerCounts> getPlayerCounts(long runId)
	{
		getJdbcTemplate().queryForList("select g.name guild_name, box_level, team_type " +
				"from dungeonrunstats_players dsp join player p on p.id = dsp.player_id join guild g on g.id = p.guild_id " +
				"where runId = ? group by g.name", runId);
		
		return null;
	}
}
