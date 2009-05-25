package org.erenda.atlantica;

import java.util.Map;

import org.erenda.atlantica.dao.CommonDao;
import org.erenda.atlantica.dao.NationDungeonDao;
import org.erenda.atlantica.domain.DungeonLevel;
import org.erenda.atlantica.manager.GhostShipManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DungeonResults 
{
	@Autowired NationDungeonDao nationDungeonDao;
	@Autowired DungeonSetupOperations dungeonSetupOperations;
	@Autowired GhostShipManager ghostShipManager;
	@Autowired CommonDao commonDao;
	
	@RequestMapping(value="/results", method=RequestMethod.POST, params="action=saveSettings")
	public String saveSettingsResults(
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
		return "redirect:/results";
	}
	
	@RequestMapping(value="/results/removePlayer")
	public String removePlayerFromEndedGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			Map<String, Object> model)
	{
		return dungeonSetupOperations.doRemovePlayer("redirect:/results", runId, playerId, model);
	}
	
	@RequestMapping("/results/changeTeam")
	public String changeTeamForEndedGhostShip(@RequestParam("run_id") long runId, @RequestParam("player_id") long playerId,
			@RequestParam("team_type") String newTeam, Map<String, Object> model)
	{
		return dungeonSetupOperations.doChangeTeam("redirect:/results", runId, playerId, newTeam, model);
	}
	
	@RequestMapping(value="/results")
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
	
	@RequestMapping(value="/results/changeBoxLevel")
	public String changeBoxLevelFromEndedGhostShip(
			@RequestParam("run_id") long runId,
			@RequestParam("player_id") long playerId,
			@RequestParam("new_level") String boxLevel,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		nationDungeonDao.changeBoxLevel(runId, playerId, DungeonLevel.valueOf(boxLevel));
		return "redirect:/results";
	}
	
	@RequestMapping(value="/results/finalize")
	public String finalizeRun(
			@RequestParam("run_id") long runId,
			Map<String, Object> model)
	{
		model.put("run_id", runId);
		nationDungeonDao.finalizeRun(runId);
		return "redirect:/results";
	}
}
