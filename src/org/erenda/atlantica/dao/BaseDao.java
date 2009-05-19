package org.erenda.atlantica.dao;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class BaseDao extends JdbcDaoSupport
{
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	protected void setDS(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	protected long getNextId(String tableName)
	{
		Long id = 0l;
		try
		{
			id = getJdbcTemplate().queryForLong("select seq from sqlite_sequence where name = ?", tableName);
		}
		catch(Exception e) { }
		return id + 1;
	}
}
