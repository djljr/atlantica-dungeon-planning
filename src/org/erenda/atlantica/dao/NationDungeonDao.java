package org.erenda.atlantica.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.erenda.atlantica.domain.Dungeon;
import org.erenda.atlantica.domain.DungeonLevel;
import org.erenda.atlantica.domain.Guild;
import org.erenda.atlantica.domain.Player;
import org.erenda.atlantica.domain.TeamType;
import org.erenda.atlantica.domain.TimestampType;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class NationDungeonDao extends BaseDao
{
	final String createStatsSql = "insert into dungeonrunstats (id, dungeon_key, dungeon_level) values (?, ?, ?)";
	public long createDungeonRun(Dungeon dungeon, Date timestamp)
	{
		synchronized (this)
		{
			long id = getNextId("dungeonrunstats");
			getJdbcTemplate().update(createStatsSql, id, dungeon, DungeonLevel.BEFORE_START);
			getJdbcTemplate().update(insertTimestampSql, id, TimestampType.CREATE_TIME, timestamp.getTime());
			getJdbcTemplate().update(dungeon.defaultsSql(id));
			return id;
		}
	}
	
	final String validRunSql = "select timestamp_time is null as valid from dungeonrunstats_timestamps where timestamp_type = ? and dungeonrun_id = ?";
	public Boolean isFinished(long runId)
	{
		List<Map<String, Object>> result = getJdbcTemplate().queryForList(validRunSql, new Object[] { TimestampType.FINISH_TIME, runId });
		if(result.size() > 0)
			return true;
		return false;
	}
	public Boolean isInPlanning(long runId)
	{
		List<Map<String, Object>> result = getJdbcTemplate().queryForList(validRunSql, new Object[] { TimestampType.START_TIME, runId });
		if(result.size() > 0)
			return false;
		return true;
	}
	public Boolean isInProgress(long runId)
	{
		return !isFinished(runId) && !isInPlanning(runId);
	}
	public boolean isFinalized(long runId)
	{
		return currentLevel(runId).equals(DungeonLevel.FINALIZED);
	}
	
	public void addPlayersToRun(long runId, long guildId, List<String> players, DungeonLevel level, Date timestamp)
	{
		if(isFinalized(runId)) return;
		List<Long> playerIds = new ArrayList<Long>();
		for(String player : players)
		{
			long id = findOrCreatePlayer(player, guildId).getId();
			if(!isPlayerOnRun(runId, id))
				playerIds.add(id);
		}
		
		addPlayersToRunInternal(runId, playerIds, level, timestamp);
	}

	final String isOnRunSql = "select player_id from dungeonrunstats_players where player_id = ? and dungeonrun_id = ?";
	
	private boolean isPlayerOnRun(long runId, long playerId)
	{
		return getJdbcTemplate().queryForList(isOnRunSql, playerId, runId).size() > 0;
	}
	
	final String addToRunSql = "insert into dungeonrunstats_players (dungeonrun_id, player_id, join_time, team_type, join_level, box_level) values (?, ?, ?, ?, ?, ?)";
	private void addPlayersToRunInternal(long runId, List<Long> playerIds, DungeonLevel level, Date timestamp)
	{
		for(long playerId : playerIds)
			getJdbcTemplate().update(addToRunSql, runId, playerId, timestamp.getTime(), TeamType.TRASH, level, level);
	}

	final String removeFromRunSql = "delete from dungeonrunstats_players where player_id = ? and dungeonrun_id = ?";
	public void removePlayerFromRun(long runId, long playerId)
	{
		if(isFinalized(runId)) return;
		getJdbcTemplate().update(removeFromRunSql, playerId, runId);
	}
	
	final String selectPlayerSql = "select p.id as player_id, p.name as player_name, p.guild_id, g.name as guild_name from player p join guild g on p.guild_id = g.id where lower(p.name) = ?";
	private Player findOrCreatePlayer(String playerName, long guildId)
	{
		Player player = findPlayer(playerName);
		if(player == null)
			player = createPlayer(playerName, guildId);
		
		return player;
	}
	
	final String createPlayerSql = "insert into player (name, guild_id) values (?, ?)";
	private Player createPlayer(String playerName, long guildId)
	{
		if(guildExists(guildId) && StringUtils.isNotBlank(playerName))
		{
			getJdbcTemplate().update(createPlayerSql, StringUtils.trim(playerName), guildId);
		}
		return findPlayer(playerName);
	}

	private boolean guildExists(long guildId)
	{
		return findGuild(guildId) != null;
	}

	final String selectGuildSql = "select g.id as guild_id, g.name as guild_name from guild g where id = ?";
	private Guild findGuild(long guildId)
	{
		return DataAccessUtils.singleResult(getJdbcTemplate().query(selectGuildSql, new Object[] { guildId }, guildMapper));
	}

	private Player findPlayer(String playerName)
	{
		return DataAccessUtils.singleResult(getJdbcTemplate().query(selectPlayerSql, new Object[] { playerName.toLowerCase() }, playerMapper ));
	}
	
	private final ParameterizedRowMapper<Player> playerMapper = new ParameterizedRowMapper<Player>()
	{
		public Player mapRow(java.sql.ResultSet row, int arg1) throws java.sql.SQLException 
		{
			Player p = new Player();
			p.setId(row.getLong("player_id"));
			p.setName(row.getString("player_name"));
			p.setGuild(guildMapper.mapRow(row, arg1));
			return p;
		};
	};
	
	private final ParameterizedRowMapper<Guild> guildMapper = new ParameterizedRowMapper<Guild>()
	{
		public Guild mapRow(java.sql.ResultSet row, int arg1) throws java.sql.SQLException 
		{
			Guild g = new Guild();
			g.setId(row.getLong("guild_id"));
			g.setName(row.getString("guild_name"));
			
			return g;
		};
	};
	
	final String runPlayersSql = "select p.id as player_id, p.name as player_name, g.name as guild_name, " +
			"datetime(s.join_time/1000, 'unixepoch', 'localtime') as join_time, s.team_type, s.join_level, s.box_level " +
			"from player p join guild g on p.guild_id = g.id join dungeonrunstats_players s on s.player_id = p.id " +
			"where dungeonrun_id = ?";
	public List<Map<String, Object>> getDungeonRoster(long runId)
	{
		return getJdbcTemplate().queryForList(runPlayersSql, runId);
	}
	
	final String teamUpdateSql = "update dungeonrunstats_players set team_type = ? where dungeonrun_id = ? and player_id = ?";
	public void changePlayerTeam(long runId, long playerId, TeamType newTeam)
	{
		if(!isFinalized(runId) && isPlayerOnRun(runId, playerId))
			getJdbcTemplate().update(teamUpdateSql, newTeam, runId, playerId);
	}
	
	final String insertTimestampSql = "insert into dungeonrunstats_timestamps (dungeonrun_id, timestamp_type, timestamp_time) values (?, ?, ?)";
	final String updateLevelSql = "update dungeonrunstats set dungeon_level = ? where id = ?";
	public void startRun(long runId, Date timestamp)
	{
		if(isInPlanning(runId))
		{
			getJdbcTemplate().update(insertTimestampSql, runId, TimestampType.START_TIME, timestamp.getTime());
			getJdbcTemplate().update(updateLevelSql, TimestampType.START_TIME.getLevel(), runId);
		}
	}
	
	final String currentLevelSql = "select dungeon_level from dungeonrunstats where id = ?";
	public DungeonLevel currentLevel(long runId)
	{
		String level = getJdbcTemplate().queryForObject(currentLevelSql, new Object[] { runId }, String.class);
		return DungeonLevel.valueOf(level);
	}
	
	final String allTimestampsSql = "select dungeonrun_id as id, timestamp_type, timestamp_time from dungeonrunstats_timestamps where dungeonrun_id = ?";
	public List<Map<String, Object>> getTimestampsForRun(long runId)
	{
		return getJdbcTemplate().queryForList(allTimestampsSql, runId);
	}
	
	public void advanceLevel(long runId, Date timestamp)
	{
		DungeonLevel current = currentLevel(runId);
		DungeonLevel next = current.getNext();
		if(next != null)
		{
			getJdbcTemplate().update(insertTimestampSql, runId, current.getTimestampType(), timestamp.getTime());
			getJdbcTemplate().update(updateLevelSql, next, runId);
		}
	}
	
	final String insertSettingsSql = "insert into dungeonrunstats_settings (dungeonrun_id, box_total, bonus_for_tower, box_less_1f, box_less_2f, box_less_3f) values (?, ?, ?, ?, ?, ?)";
	final String setSettingsSql = "update dungeonrunstats_settings set box_total = ?, bonus_for_tower = ?, box_less_1f = ?, box_less_2f = ?, box_less_3f = ? where dungeonrun_id = ?";
	final String selectSettingsSql = "select box_total, bonus_for_tower, box_less_1f, box_less_2f, box_less_3f from dungeonrunstats_settings where dungeonrun_id = ?";
	public void setSettings(long runId, long boxTotal, long bonusForTower,
			long boxLess1F, long boxLess2F, long boxLess3F)
	{
		getJdbcTemplate().update(setSettingsSql, boxTotal, bonusForTower, boxLess1F, boxLess2F, boxLess3F, runId);
	}
	
	public Map<String, Object> getSettings(long runId)
	{
		return getJdbcTemplate().queryForMap(selectSettingsSql, runId);
	}
	
	final String guildCountSql = "select guild_id, g.name as guild_name, count(*) as count " +
			"from dungeonrunstats_players dsp join player p on p.id = dsp.player_id join guild g on g.id = p.guild_id " +
			"where dungeonrun_id = ? group by g.name, guild_id";
	public List<Map<String, Object>> getGuildCounts(long runId)
	{
		return getJdbcTemplate().queryForList(guildCountSql, runId);
	}
	
	final String changeBoxLevelSql = "update dungeonrunstats_players set box_level = ? where dungeonrun_id = ? and player_id = ?";
	public void changeBoxLevel(long runId, long playerId, DungeonLevel boxLevel)
	{
		getJdbcTemplate().update(changeBoxLevelSql, boxLevel, runId, playerId);	
	}
	public void finalizeRun(long runId)
	{
		getJdbcTemplate().update(updateLevelSql, DungeonLevel.FINALIZED, runId);
	}
	
	final String findAllRunsSql = "select id, dungeon_key, dungeon_level, create_time, count(*) as players " +
			"from dungeonrunstats ds join dungeonrunstats_players dsp on dsp.dungeonrun_id = ds.id " +
			" join (select dungeonrun_id, datetime(timestamp_time/1000, 'unixepoch', 'localtime') as create_time from dungeonrunstats_timestamps where timestamp_type = 'CREATE_TIME') dst_create on dst_create.dungeonrun_id = ds.id " +
			"group by id, dungeon_key, dungeon_level, dst_create.create_time";
	public List<Map<String, Object>> findAllRuns()
	{
		return getJdbcTemplate().queryForList(findAllRunsSql);
	}
}