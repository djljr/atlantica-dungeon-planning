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
		String[] playerArray = players.split("\\s*,\\s*");
		nationDungeonDao.addPlayersToRun(runId, guildId, Arrays.asList(playerArray), new Date());
		model.put("run_id", runId);
		return "redirect:/planning/ghostship";
	}
	
	@RequestMapping(value="/planning/ghostship/removePlayer")
	public String removePlayerFromGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		nationDungeonDao.removePlayerFromRun(runId, playerId);
		model.put("run_id", runId);
		return "redirect:/planning/ghostship";
	}

	@RequestMapping(value="/planning/ghostship/changeTeam")
	public String changeTeamForGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			@RequestParam("team_type") String newTeam,
			Map<String, Object> model)
	{
		nationDungeonDao.changePlayerTeam(runId, playerId, TeamType.valueOf(newTeam));
		model.put("run_id", runId);
		return "redirect:/planning/ghostship";
	}
	
	@RequestMapping(value="/planning/ghostship/startRun")
	public String startGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		nationDungeonDao.startRun(runId, new Date());
		model.put("run_id", runId);
		return "redirect:/manage/ghostship";
	}
	
	@RequestMapping(value="/manage/ghostship")
	public String manageGhostShip(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		return "Dungeon/Manage/ghostShip";
	}
}