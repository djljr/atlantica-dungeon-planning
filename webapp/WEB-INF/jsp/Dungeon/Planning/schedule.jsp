<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title>Planning</title></head>
<body>
<h1>Planning - List</h1>
<display:table name="runs" id="run">
	<display:column><a href="select?run_id=${run.id}">${run.dungeon_key}</a></display:column>
	<display:column property="dungeon_level" />
	<display:column property="create_time" />
	<display:column property="players" />
</display:table>
<button onclick="location.href='planning/ghostship/create'">New Ghost Ship</button>
</body>
</html>