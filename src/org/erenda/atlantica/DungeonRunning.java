package org.erenda.atlantica;

import java.util.Date;
import java.util.Map;

import org.erenda.atlantica.dao.CommonDao;
import org.erenda.atlantica.dao.NationDungeonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class DungeonRunning 
{
	@Autowired DungeonSetupOperations dungeonSetupOperations;
	@Autowired NationDungeonDao nationDungeonDao;
	@Autowired CommonDao commonDao;
	
	@RequestMapping(value="/manage", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToRunningGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("guild_id") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doAddPlayers("redirect:/manage", runId, guildId, players, model);
	}
	
	@RequestMapping("/manage/removePlayer")
	public String removePlayerFromRunningGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doRemovePlayer("redirect:/manage", runId, playerId, model);
	}
	
	@RequestMapping("/manage/changeTeam")
	public String changeTeamForRunningGhostShip(@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId, @RequestParam("team_type") String newTeam,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doChangeTeam("redirect:/manage", runId, playerId, newTeam, model);
	}
	
	@RequestMapping(value="/manage")
	public String manageGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isInPlanning(runId))
			return "redirect:/planning";
		if(nationDungeonDao.isFinished(runId))
			return "redirect:/results";
		
		model.put("settings", nationDungeonDao.getSettings(runId));
		model.put("guilds", commonDao.allGuilds());
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		model.put("currentFloor", nationDungeonDao.currentLevel(runId));
		return "Dungeon/Manage/manage";
	}
	
	@RequestMapping(value="/manage/advance")
	public String advanceToNextLevel(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(!nationDungeonDao.isInProgress(runId))
			return "redirect:/manage";
		
		nationDungeonDao.advanceLevel(runId, new Date());
		return "redirect:/manage";
	}
}
