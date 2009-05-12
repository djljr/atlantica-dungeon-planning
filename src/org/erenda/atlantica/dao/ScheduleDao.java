package org.erenda.atlantica.dao;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDao extends JdbcDaoSupport
{
	public Integer getValue() 
	{ 
		return 30; 
	}
	public Long getDBValue() 
	{ 
		return getJdbcTemplate().queryForLong("select id from foo where id = 3"); 
	}
	
	@Autowired
	protected void setDS(DataSource dataSource) {
		setDataSource(dataSource);
	}
}
