/*
 * Copyright (C) 2008-2010 Surevine Limited.
 *
 * Although intended for deployment and use alongside Alfresco this module should
 * be considered 'Not a Contribution' as defined in paragraph 1 bullet 4 of Alfrescos
 * standard contribution agreement, see
 * http://www.alfresco.org/resource/AlfrescoContributionAgreementv2.pdf
 *
 * This is free software: you can redistribute 
 * and/or modify it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * To see a copy of the GNU Lesser General Public License visit
 * <http://www.gnu.org/licenses/>.
 */
package com.surevine.alfresco.audit.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.surevine.alfresco.audit.AuditRepository;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AuditEventListener.EventType;

/**
 * Simple JDBC based implementation of the audit repository.
 * 
 * @author garethferrier
 *
 */
public class JdbcAuditRepository implements AuditRepository {

    
    /**
     * Spring helper for writing to the database.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Setter used by spring.
     * 
     * @param dataSource to initialise the JDBCTemplate with.
     */
    public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

    private static final String UPDATE_SQL = "insert into ALF_ACCOUNTING_AUDIT (username, action, source, secLabel, site, details, url, version, tstamp, remote_addr, success, tags, node_ref, time_spent)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String GET_ACTIVITY_COUNT_SQL_WITH_PATH_FILTER = "SELECT count(1) FROM alf_accounting_audit WHERE tstamp > ? and action = ? and path like ?";
    
    private static final String GET_ACTIVITY_COUNT_SQL = "SELECT count(1) FROM alf_accounting_audit WHERE tstamp > ? and action = ?";


	/**
	 * Simple write to the database of the audit object.
	 * 
	 * @see com.surevine.alfresco.audit.AuditRepository#audit(com.surevine.alfresco.audit.Auditable)
	 */
	public void audit(final Auditable toAudit) {
	    this.jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, truncate(toAudit.getUser(), 40));
                ps.setString(2,  truncate(toAudit.getAction(), 40));
                ps.setString(3, truncate(toAudit.getSource(), 80));
                ps.setString(4, truncate(toAudit.getSecLabel(), 1024));
                ps.setString(5, truncate(toAudit.getSite(), 80));
                ps.setString(6, truncate(toAudit.getDetails(), 256));
                ps.setString(7, truncate(toAudit.getUrl(), 256));
                ps.setString(8, truncate(toAudit.getVersion(), 10));
                ps.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setString(10, truncate(toAudit.getRemoteAddress(), 40));
                ps.setString(11, Boolean.toString(toAudit.isSuccess()));
                ps.setString(12, truncate(toAudit.getTags(), 256));
                ps.setString(13, truncate(toAudit.getNodeRef(), 80));
                ps.setLong(14, toAudit.getTimeSpent());
            }
        });
	}
	
	public static String truncate(final String truncate, int length) {
	    if (truncate != null) {
	        if (truncate.length() < length) {
	            return truncate;
	        } else {
	            return (truncate.substring(0, length));
	        }    
	    } else {
	        return truncate;
	    }
	    
	}
	
	public ResultCount getResultCount(Date startPoint, final EventType eventType, String pathFilter)
	{
	 
	 if (startPoint==null)
	 {
	     startPoint=new Date(0l);
	 }
	 
	 String sql = GET_ACTIVITY_COUNT_SQL;
	 boolean usingPathFilter=false;
	 
	 if (pathFilter!=null && pathFilter.trim().length()>0)
	 {
	     pathFilter="%"+pathFilter+"%";
	     sql = GET_ACTIVITY_COUNT_SQL_WITH_PATH_FILTER;
	     usingPathFilter=true;
	 }
	 
	 if (eventType==null)
	 {
	     throw new RuntimeException("An eventType must be supplied to getResultCount");
	 }
	 
	 Object[] params;
	 if (usingPathFilter)
	 {
	     params = new Object[] {startPoint, eventType, pathFilter};
	 }
	 else
	 {
	       params = new Object[] {startPoint, eventType};
	 }
	 
	 return new ResultCount(startPoint, jdbcTemplate.queryForInt(sql, params));
	 
	}
	
}
