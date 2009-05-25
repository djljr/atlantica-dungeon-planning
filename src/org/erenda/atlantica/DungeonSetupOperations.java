package org.erenda.atlantica;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.TeamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DungeonSetupOperations 
{
	@Autowired NationDungeonDao nationDungeonDao;
	
	String doAddPlayers(String redirect, long runId, long guildId, String players, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isFinalized(runId))
			return "redirect:/results";
		
		String[] playerArray = players.split("\\s*,\\s*");
		nationDungeonDao.addPlayersToRun(runId, guildId, Arrays.asList(playerArray), nationDungeonDao.currentLevel(runId), new Date());
		return redirect;
	}
	
	String doRemovePlayer(String redirect, long runId, long playerId, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isFinalized(runId))
			return "redirect:/results";
		
		nationDungeonDao.removePlayerFromRun(runId, playerId);
		return redirect;
	}
	
	String doChangeTeam(String redirect, long runId, long playerId, String newTeam, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isFinalized(runId))
			return "redirect:/results";

		nationDungeonDao.changePlayerTeam(runId, playerId, TeamType.valueOf(newTeam));
		return redirect;
	}
}
