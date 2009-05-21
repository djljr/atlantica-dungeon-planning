<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title>Ghost Ship</title></head>
<body>
<h1>Ghost Ship - Results</h1>
<fieldset>
<legend>Settings</legend>
<form name="settings" method="post">
Total Boxes: <input type="text" name="box_total" value="${settings.box_total}" /><br />
Bonus for Tower Team: <input type="text" name="bonus_for_tower" value="${settings.bonus_for_tower}"/><br />
Box Penalty:<br />
Join on 1st Floor: <input type="text" name="box_less_1f" value="${settings.box_less_1f}"/><br />
Join on 2nd Floor: <input type="text" name="box_less_2f" value="${settings.box_less_2f}"/><br />
Join on 3rd Floor: <input type="text" name="box_less_3f" value="${settings.box_less_3f}"/><br />
<input type="hidden" name="action" value="saveSettings" />
<button type="submit">Save</button>
</form>
</fieldset>

<fieldset>
<legend>Results</legend>
<display:table name="dungeonResults.guildCounts" id="gc">
	<display:column property="guild" />
	<display:column property="total" />
	<display:column property="tower" />
	<display:column property="firstFloor" />
	<display:column property="secondFloor" />
	<display:column property="thirdFloor" />
	<display:column property="numBoxes" />
</display:table>
Boxes Distributed: ${dungeonResults.distributedBoxTotal}<br />
Boxes Per Player: ${dungeonResults.boxesPerPlayer}
</fieldset>

<fieldset>
<legend>Current Roster</legend>
<display:table name="currentRoster" id="p" requestURI="${requestURI}">
	<display:column title="Name" property="player_name" sortable="true" />
	<display:column title="Guild" property="guild_name" sortable="true" />
	<display:column title="Join Time" property="join_time" sortable="true" />
	<display:column title="Join Floor" property="join_level" sortable="true" />
	<display:column title="Box Level">
		<select onchange="javascript:location.href='ghostship/changeBoxLevel?player_id=${p.player_id}&run_id=${param.run_id}&new_level='+this.value;">
			<c:forEach items="${boxLevels}" var="bi">
			<option value="${bi}" <c:if test="${p.box_level eq bi}">selected="selected"</c:if>>${bi}</option>
			</c:forEach>
		</select>
	</display:column>
	<display:column>
		<c:if test="${p.team_type != 'TRASH'}"><a href="ghostship/changeTeam?player_id=${p.player_id}&run_id=${param.run_id}&team_type=TRASH">Trash</a></c:if>
		<c:if test="${p.team_type == 'TRASH'}"><span style="font-weight:bold">Trash</span></c:if>
		<c:if test="${p.team_type != 'GATE_TOWER'}"><a href="ghostship/changeTeam?player_id=${p.player_id}&run_id=${param.run_id}&team_type=GATE_TOWER">Gate / Tower</a></c:if>
		<c:if test="${p.team_type == 'GATE_TOWER'}"><span style="font-weight:bold">Gate / Tower</span></c:if>
	</display:column>
	<display:column>
		<a href="ghostship/removePlayer?run_id=${param.run_id}&player_id=${p.player_id}">remove</a>
	</display:column>	
</display:table>
</fieldset>

<fieldset>
<legend>Participation</legend>
<c:forEach items="${counts}" var="g" varStatus="stat">
${g.guild_name}: ${g.count}<c:if test="${!stat.last}">,</c:if>
</c:forEach>
</fieldset>

<fieldset>
<legend>Add Players</legend>
<form name="bulkAdd" method="post">
<label for="bulkAddPlayers" style="display:block">Bulk:</label>
<select name="guild_id">
	<option value="-1">--Select Guild</option>
<c:forEach items="${guilds}" var="guild">
	<option value="${guild.id}">${guild.name}</option>
</c:forEach>
</select>
<br />
<textarea name="players" rows="3" cols="30"></textarea>
<br />
<input type="hidden" name="action" value="addPlayers" />
<button type="submit">Add</button>
</form>
</fieldset>

<button onclick="location.href='ghostship/finalize?run_id=${param.run_id}'">Finalize</button>
</body>
</html>