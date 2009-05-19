<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title>Ghost Ship</title></head>
<body>
<h1>Ghost Ship</h1>
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

<fieldset>
<legend>Current Roster</legend>
<display:table name="currentRoster" id="p">
	<display:column title="Name" property="player_name" />
	<display:column title="Guild" property="guild_name" />
	<display:column title="Join Time" property="join_time" />
	<display:column>
		<a href="ghostship/removePlayer?run_id=${param.runId}&player_id=${p.player_id}">remove</a>
	</display:column>
	<display:column>
		<c:if test="${p.team_type != 'TRASH'}"><a href="ghostship/changeTeam?player_id=${p.player_id}&run_id=${param.run_id}&team_type=TRASH">Trash</a></c:if>
		<c:if test="${p.team_type == 'TRASH'}"><span style="font-weight:bold">Trash</span></c:if>
		<c:if test="${p.team_type != 'GATE_TOWER'}"><a href="ghostship/changeTeam?player_id=${p.player_id}&run_id=${param.run_id}&team_type=GATE_TOWER">Gate / Tower</a></c:if>
		<c:if test="${p.team_type == 'GATE_TOWER'}"><span style="font-weight:bold">Gate / Tower</span></c:if>
	</display:column>
</display:table>
</fieldset>
</body>
</html>