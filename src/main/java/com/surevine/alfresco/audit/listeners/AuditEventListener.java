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
package com.surevine.alfresco.audit.listeners;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public interface AuditEventListener {

    /**
     * Intent is to populate this enum as required.  It should eventually contain every possible value for the action column in a single easy-to-read place
     * @author simonw
     *
     */
    public enum EventType
    {
        WIKI_PAGE_VIEW(ViewWikiPageAuditEventListener.ACTION);
        
        private String _databaseValue;
        
        EventType(String dbValue)
        {
            _databaseValue=dbValue;
        }
        
        public String getDBValue() 
        { 
            return _databaseValue; 
        }
        
    }
    
    /**
     * @param request
     * @param response
     * @param postContent
     * @param toAudit TODO
     * @throws JSONException 
     * @throws Exception
     */
    List<Auditable> populateAuditItems(HttpServletRequest request, HttpServletResponse response, String postContent) throws JSONException;

    /**
     * @return
     */
    String getMethod();

    String getAction();

    boolean isEventFired(HttpServletRequest request, String postContent);

    void decideSuccess(BufferedHttpServletResponse response, Auditable audit) throws JSONException, IOException;
    
}
