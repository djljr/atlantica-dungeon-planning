package org.erenda.atlantica;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.erenda.atlantica.dao.CommonDao;
import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.Dungeon;
import org.erenda.atlantica.domain.TeamType;
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

	@RequestMapping("/schedule")
	public String index(Map<String, Object> model)
	{
		return "Dungeon/Planning/schedule";
	}

	@RequestMapping("/planning")
	public String planningIndex(Map<String, Object> model)
	{
		return "Dungeon/Planning/index";
	}

	@RequestMapping(value="/planning/ghostship/create")
	public String createGhostShipRun(Map<String, Object> model)
	{
		model.put("run_id", nationDungeonDao.createDungeonRun(Dungeon.GHOST_SHIP, new Date()));
		return "redirect:/planning/ghostship";
	}

	@RequestMapping(value="/planning/ghostship", method=RequestMethod.GET)
	public String planningGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(!nationDungeonDao.isInPlanning(runId) && !nationDungeonDao.isFinished(runId))
			return "redirect:/manage/ghostship";
		else if(!nationDungeonDao.isInPlanning(runId))
			return "redirect:/results/ghostship";
		
		model.put("guilds", commonDao.allGuilds());
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		return "Dungeon/Planning/ghostShip";
	}
	
	@RequestMapping(value="/planning/ghostship", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("guild_id") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		return doAddPlayers("redirect:/planning/ghostship", runId, guildId, players, model);
	}
	
	@RequestMapping(value="/manage/ghostship", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToRunningGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("guild_id") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		return doAddPlayers("redirect:/manage/ghostship", runId, guildId, players, model);
	}
	
	private String doAddPlayers(String redirect, long runId, long guildId, String players, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isFinalized(runId))
			return "redirect:/results/ghostship";
		
		String[] playerArray = players.split("\\s*,\\s*");
		nationDungeonDao.addPlayersToRun(runId, guildId, Arrays.asList(playerArray), nationDungeonDao.currentLevel(runId), new Date());
		return redirect;
	}
	
	@RequestMapping("/manage/ghostship/removePlayer")
	public String removePlayerFromRunningGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		return doRemovePlayer("redirect:/manage/ghostship", runId, playerId, model);
	}
	
	@RequestMapping(value="/planning/ghostship/removePlayer")
	public String removePlayerFromGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		return doRemovePlayer("redirect:/planning/ghostship", runId, playerId, model);
	}

	private String doRemovePlayer(String redirect, long runId, long playerId, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isFinalized(runId))
			return "redirect:/results/ghostship";
		
		nationDungeonDao.removePlayerFromRun(runId, playerId);
		return redirect;
	}
	
	@RequestMapping("/manage/ghostship/changeTeam")
	public String changeTeamForRunningGhostShip(@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId, @RequestParam("team_type") String newTeam,
			Map<String, Object> model)
	{
		return doChangeTeam("redirect:/manage/ghostship", runId, playerId, newTeam, model);
	}
	
	@RequestMapping("/planning/ghostship/changeTeam")
	public String changeTeamForGhostShip(@RequestParam("run_id") long runId, @RequestParam("player_id") long playerId,
			@RequestParam("team_type") String newTeam, Map<String, Object> model)
	{
		return doChangeTeam("redirect:/planning/ghostship", runId, playerId, newTeam, model);
	}
	
	private String doChangeTeam(String redirect, long runId, long playerId, String newTeam, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isFinalized(runId))
			return "redirect:/results/ghostship";

		nationDungeonDao.changePlayerTeam(runId, playerId, TeamType.valueOf(newTeam));
		return redirect;
	}
	
	@RequestMapping(value="/planning/ghostship/startRun")
	public String startGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(!nationDungeonDao.isInPlanning(runId))
			return "redirect:/manage/ghostship";
		
		nationDungeonDao.startRun(runId, new Date());
		return "redirect:/manage/ghostship";
	}
	
	@RequestMapping(value="/manage/ghostship")
	public String manageGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("guilds", commonDao.allGuilds());
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		model.put("currentFloor", nationDungeonDao.currentLevel(runId));
		return "Dungeon/Manage/ghostShip";
	}
	
	@RequestMapping(value="/manage/ghostship/advance")
	public String advanceToNextLevel(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(!nationDungeonDao.isInProgress(runId))
			return "redirect:/manage/ghostship";
		
		nationDungeonDao.advanceLevel(runId, new Date());
		return "redirect:/manage/ghostship";
	}
	
	@RequestMapping(value="/results/ghostship")
	public String showResults(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		if(nationDungeonDao.isFinalized(runId))
		{
			return "Dungeon/Results/resultsFinal";	
		}
		else
		{
			return "Dungeon/Results/results";	
		}
	}
}