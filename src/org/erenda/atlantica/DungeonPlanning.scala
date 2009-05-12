package org.erenda.atlantica

import org.springframework.stereotype._
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class DungeonPlanning 
{
	@RequestMapping 
	def index:String = { "DungeonPlanning/schedule" }
}
