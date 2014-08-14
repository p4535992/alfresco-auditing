/*
 * Copyright (C) 2008-2010 Surevine Limited.
 * 
 * Although intended for deployment and use alongside Alfresco this module should
 * be considered 'Not a Contribution' as defined in Alfresco'sstandard contribution agreement, see
 * http://www.alfresco.org/resource/AlfrescoContributionAgreementv2.pdf
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
     * @throws JSONException 
     * @throws Exception
     */
    List<Auditable> populateAuditItems(HttpServletRequest request, HttpServletResponse response) throws JSONException;

    /**
     * @return
     */
    String getMethod();

    String getAction();

    boolean isEventFired(HttpServletRequest request);

    void decideSuccess(BufferedHttpServletResponse response, Auditable audit) throws JSONException, IOException;
    
}
