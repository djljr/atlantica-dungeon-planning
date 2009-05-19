<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Ghost Ship</title></head>
<body>
<h1>Ghost Ship</h1>
<fieldset>
<legend>Add Players</legend>
<form name="bulkAdd" method="post">
<label for="bulkAddPlayers" style="display:block">Bulk:</label>
<select name="guildId">
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
<table>
<tr><th>Name</th><th>Guild</th><th>Join Time</th><th></th></tr>
<c:forEach items="${currentRoster}" var="p">
<tr><td>${p.player_name}</td><td>${p.guild_name}</td><td>${p.join_time}</td><td><a href="ghostship/removePlayer?runId=${param.runId}&playerId=${p.player_id}">remove</a></td></tr>
</c:forEach>
</table>
</fieldset>
</body>
</html>