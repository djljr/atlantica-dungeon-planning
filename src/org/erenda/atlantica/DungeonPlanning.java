package org.erenda.atlantica;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.erenda.atlantica.dao.CommonDao;
import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.Dungeon;
import org.erenda.atlantica.domain.DungeonLevel;
import org.erenda.atlantica.domain.TeamType;
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
	
	@RequestMapping("/schedule")
	public String index(Map<String, Object> model)
	{
		model.put("runs", nationDungeonDao.findAllRuns());
		return "Dungeon/Planning/schedule";
	}
	
	@RequestMapping("/select")
	public String selectPage(@RequestParam("run_id") long runId, Map<String, Object> model)
	{
		model.put("run_id", runId);
		if(nationDungeonDao.isInPlanning(runId))
			return "redirect:/planning/ghostship";
		else if(!nationDungeonDao.isInPlanning(runId) && !nationDungeonDao.isFinished(runId))
			return "redirect:/manage/ghostship";
		else if(!nationDungeonDao.isInPlanning(runId))
			return "redirect:/results/ghostship";
		else return "redirect:/planning";
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
		
		model.put("settings", nationDungeonDao.getSettings(runId));
		model.put("guilds", commonDao.allGuilds());
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		return "Dungeon/Planning/planGhostShip";
	}
	
	@RequestMapping(value="/planning/ghostship", method=RequestMethod.POST, params="action=saveSettings")
	public String saveSettingsPlanning(
			@RequestParam("run_id") long runId,
			@RequestParam("box_total") long boxTotal,
			@RequestParam("bonus_for_tower") long bonusForTower,
			@RequestParam("box_less_1f") long boxLess1F,
			@RequestParam("box_less_2f") long boxLess2F,
			@RequestParam("box_less_3f") long boxLess3F,
			Map<String, Object> model)
	{
		nationDungeonDao.setSettings(runId, boxTotal, bonusForTower, boxLess1F, boxLess2F, boxLess3F);
		return "redirect:/planning/ghostship";
	}
	
	@RequestMapping(value="/results/ghostship", method=RequestMethod.POST, params="action=saveSettings")
	public String saveSettingsResults(
			@RequestParam("run_id") long runId,
			@RequestParam("box_total") long boxTotal,
			@RequestParam("bonus_for_tower") long bonusForTower,
			@RequestParam("box_less_1f") long boxLess1F,
			@RequestParam("box_less_2f") long boxLess2F,
			@RequestParam("box_less_3f") long boxLess3F,
			Map<String, Object> model)
	{
		nationDungeonDao.setSettings(runId, boxTotal, bonusForTower, boxLess1F, boxLess2F, boxLess3F);
		return "redirect:/results/ghostship";
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
	
	@RequestMapping(value="/results/ghostship", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToEndedGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("guild_id") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		return doAddPlayers("redirect:/results/ghostship", runId, guildId, players, model);
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

	@RequestMapping(value="/results/ghostship/removePlayer")
	public String removePlayerFromEndedGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		return doRemovePlayer("redirect:/results/ghostship", runId, playerId, model);
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
	
	@RequestMapping("/results/ghostship/changeTeam")
	public String changeTeamForEndedGhostShip(@RequestParam("run_id") long runId, @RequestParam("player_id") long playerId,
			@RequestParam("team_type") String newTeam, Map<String, Object> model)
	{
		return doChangeTeam("redirect:/results/ghostship", runId, playerId, newTeam, model);
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
		model.put("run_id", runId);
		if(nationDungeonDao.isInPlanning(runId))
			return "redirect:/planning/ghostship";
		if(nationDungeonDao.isFinished(runId))
			return "redirect:/results/ghostship";
		
		model.put("settings", nationDungeonDao.getSettings(runId));
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
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		model.put("dungeonResults", ghostShipManager.getResultsForDungeon(runId));
		
		if(nationDungeonDao.isFinalized(runId))
		{
			return "Dungeon/Results/resultsFinal";	
		}
		else
		{
			model.put("boxLevels", DungeonLevel.boxValues());
			model.put("counts", nationDungeonDao.getGuildCounts(runId));
			model.put("settings", nationDungeonDao.getSettings(runId));
			model.put("guilds", commonDao.allGuilds());
			
			return "Dungeon/Results/results";	
		}
	}
	
	@RequestMapping(value="/results/ghostship/changeBoxLevel")
	public String changeBoxLevelFromEndedGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			@RequestParam("new_level") String boxLevel,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		nationDungeonDao.changeBoxLevel(runId, playerId, DungeonLevel.valueOf(boxLevel));
		return "redirect:/results/ghostship";
	}
	
	@RequestMapping(value="/results/ghostship/finalize")
	public String finalizeRun(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		nationDungeonDao.finalizeRun(runId);
		return "redirect:/results/ghostship";
	}
}