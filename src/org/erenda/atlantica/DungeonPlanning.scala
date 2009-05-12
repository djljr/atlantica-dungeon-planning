
package org.erenda.atlantica

import java.util._
import org.springframework.stereotype._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.erenda.atlantica.dao._

@Controller
class DungeonPlanning(@Autowired scheduleDao:ScheduleDao) 
{ 
  def this() = this(null)
 
  @RequestMapping(Array("/schedule"))
  def index(model:Map[String,Object]):String = 
  {
	model.put("value", scheduleDao.getValue().toString)
	model.put("dbvalue", scheduleDao.getDBValue().toString)
    "DungeonPlanning/schedule"
  }
}