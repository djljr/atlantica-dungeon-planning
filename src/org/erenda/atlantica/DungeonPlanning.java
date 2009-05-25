package org.erenda.atlantica;

import java.util.Date;
import java.util.Map;

import org.erenda.atlantica.dao.CommonDao;
import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.Dungeon;
import org.erenda.atlantica.manager.GhostShipManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DungeonPlanning
{
	@Autowired CommonDao commonDao;
	@Autowired NationDungeonDao nationDungeonDao;
	@Autowired GhostShipManager ghostShipManager;
	@Autowired DungeonSetupOperations dungeonSetupOperations;
	

	@RequestMapping(value="/planning/create")
	public String createGhostShipRun(Map<String, Object> model)
	{
		model.put("run_id", nationDungeonDao.createDungeonRun(Dungeon.GHOST_SHIP, new Date()));
		return "redirect:/planning";
	}

	@RequestMapping(value="/planning", method=RequestMethod.GET)
	public String planningGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(!nationDungeonDao.isInPlanning(runId) && !nationDungeonDao.isFinished(runId))
			return "redirect:/manage";
		else if(!nationDungeonDao.isInPlanning(runId))
			return "redirect:/results";
		
		model.put("settings", nationDungeonDao.getSettings(runId));
		model.put("guilds", commonDao.allGuilds());
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		return "Dungeon/Planning/plan";
	}
	
	@RequestMapping(value="/planning", method=RequestMethod.POST, params="action=saveSettings")
	public String saveSettingsPlanning(
			@RequestParam("run_id") long runId,
			@RequestParam("box_total") long boxTotal,
			@RequestParam("bonus_for_tower") long bonusForTower,
			@RequestParam("box_less_1f") long boxLess1F,
			@RequestParam("box_less_2f") long boxLess2F,
			@RequestParam("box_less_3f") long boxLess3F,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		nationDungeonDao.setSettings(runId, boxTotal, bonusForTower, boxLess1F, boxLess2F, boxLess3F);
		return "redirect:/planning";
	}
	
	@RequestMapping(value="/planning", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("guild_id") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doAddPlayers("redirect:/planning", runId, guildId, players, model);
	}
	
	@RequestMapping(value="/results/ghostship", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToEndedGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("guild_id") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doAddPlayers("redirect:/results", runId, guildId, players, model);
	}
	
	@RequestMapping(value="/planning/removePlayer")
	public String removePlayerFromGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doRemovePlayer("redirect:/planning", runId, playerId, model);
	}
	
	@RequestMapping("/planning/changeTeam")
	public String changeTeamForGhostShip(@RequestParam("run_id") long runId, @RequestParam("player_id") long playerId,
			@RequestParam("team_type") String newTeam, Map<String, Object> model)
	{
		return dungeonSetupOperations.doChangeTeam("redirect:/planning", runId, playerId, newTeam, model);
	}
	
	@RequestMapping(value="/planning/startRun")
	public String startGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(!nationDungeonDao.isInPlanning(runId))
			return "redirect:/manage";
		
		nationDungeonDao.startRun(runId, new Date());
		return "redirect:/manage";
	}
}