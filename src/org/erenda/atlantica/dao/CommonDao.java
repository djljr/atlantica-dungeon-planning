package org.erenda.atlantica.dao;

import java.sql.ResultSet;
import java.util.Collection;

import org.erenda.atlantica.domain.Guild;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDao extends BaseDao
{
	private static final String guildSql = "select id, name from guild";
	public Collection<Guild> allGuilds()
	{
		Collection<Guild> retList = getJdbcTemplate().query(guildSql, guildMapper);
		return retList;
	}
	
	private static final ParameterizedRowMapper<Guild> guildMapper = new ParameterizedRowMapper<Guild>() {
		public Guild mapRow(ResultSet row, int arg1) throws java.sql.SQLException 
		{
			Guild g = new Guild();
			g.setId(row.getLong("id"));
			g.setName(row.getString("name"));
			return g;
		};
	};
}
