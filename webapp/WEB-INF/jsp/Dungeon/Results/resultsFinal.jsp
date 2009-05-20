<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title>Ghost Ship</title></head>
<body>
<h1>Ghost Ship - Results</h1>


<fieldset>
<legend>Roster</legend>
<display:table name="currentRoster" id="p">
	<display:column title="Name" property="player_name" sortable="true" />
	<display:column title="Guild" property="guild_name" sortable="true" />
	<display:column title="Join Time" property="join_time" sortable="true" />
	<display:column title="Join Floor" property="join_level" sortable="true" />
	<display:column title="Team" property="team_type" sortable="true" />
</display:table>
</fieldset>
</body>
</html>