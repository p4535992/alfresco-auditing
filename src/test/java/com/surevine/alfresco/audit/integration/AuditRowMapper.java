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
package com.surevine.alfresco.audit.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;

/**
 * @author garethferrier
 * 
 */
@SuppressWarnings("rawtypes")
public class AuditRowMapper implements RowMapper {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public Object mapRow(ResultSet rs, int rowCount) throws SQLException {

        Auditable audited = new AuditItem();
        audited.setAction(rs.getString("action"));
        audited.setSource(rs.getString("source"));
        audited.setVersion(rs.getString("version"));
        audited.setRemoteAddress(rs.getString("remote_addr"));
        audited.setUser(rs.getString("username"));
        audited.setDetails(rs.getString("details"));
        audited.setSuccess(rs.getBoolean("success"));
        audited.setUrl(rs.getString("url"));
        audited.setDate(new Date(rs.getTimestamp("tstamp").getTime()));
        audited.setSite(rs.getString("site"));
        audited.setVersion(rs.getString("version"));
        audited.setSecLabel(rs.getString("secLabel"));
        audited.setTags(rs.getString("tags"));
        audited.setNodeRef(rs.getString("node_ref"));

        return audited;
    }
}
