package org.erenda.atlantica;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.erenda.atlantica.dao.CommonDao;
import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.Dungeon;
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
		model.put("runId", nationDungeonDao.createDungeonRun(Dungeon.GHOST_SHIP));
		return "redirect:/planning/ghostship";
	}

	@RequestMapping(value="/planning/ghostship", method=RequestMethod.GET)
	public String planningGhostShip(
			@RequestParam("runId") long runId,
			Map<String, Object> model)
	{
		model.put("guilds", commonDao.allGuilds());
		model.put("currentRoster", nationDungeonDao.getDungeonRoster(runId));
		return "Dungeon/Planning/ghostShip";
	}
	
	@RequestMapping(value="/planning/ghostship", method=RequestMethod.POST, params="action=addPlayers")
	public String addPlayersToGhostShip(
			@RequestParam("runId") long runId,
			@RequestParam("guildId") long guildId,
			@RequestParam("players") String players,
			Map<String, Object> model)
	{
		String[] playerArray = players.split("\\s*,\\s*");
		nationDungeonDao.addPlayersToRun(runId, guildId, Arrays.asList(playerArray), new Date());
		model.put("runId", runId);
		return "redirect:/planning/ghostship";
	}
	
	@RequestMapping(value="/planning/ghostship/removePlayer")
	public String removePlayerFromGhostShip(
			@RequestParam("runId") long runId,
			@RequestParam("playerId") long playerId,
			Map<String, Object> model)
	{
		nationDungeonDao.removePlayerFromRun(runId, playerId);
		model.put("runId", runId);
		return "redirect:/planning/ghostship";
	}
}