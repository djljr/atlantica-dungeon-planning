package org.erenda.atlantica;

import java.util.Map;

import org.erenda.atlantica.dao.NationDungeonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RunList 
{
	@Autowired NationDungeonDao nationDungeonDao;
	
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
			return "redirect:/planning";
		else if(!nationDungeonDao.isInPlanning(runId) && !nationDungeonDao.isFinished(runId))
			return "redirect:/manage";
		else if(!nationDungeonDao.isInPlanning(runId))
			return "redirect:/results";
		else return "redirect:/planning";
	}
}
