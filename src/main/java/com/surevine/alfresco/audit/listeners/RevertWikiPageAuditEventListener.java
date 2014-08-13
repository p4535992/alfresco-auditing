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

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.JSONException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * Audit listener for wiki page reversions.  This is awkward as a reversion is just an update to a content value that <i>happens</i> to be the 
 * same as a previous version - the conversation between share and alfresco (indeed, between the client and alfresco) on reverting wiki pages happens
 * without reference to version numbers(!).  There's therefore less information in this audit event than we would like.=, but it should still be sufficent.
 * @author simonw
 *
 */
public class RevertWikiPageAuditEventListener  extends PutAuditEventListener {

    /**
     * String literal to denote the type of action taken.
     */
    private static final String ACTION = "WIKI_PAGE_REVERTED";
    
    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(RevertWikiPageAuditEventListener.class);

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/page/";

    /**
     * Default constructor which provides statics to the super class.
     */
    public RevertWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }
    
    /**
     * Name expected of the page value in JSON.
     */
    public static final String JSON_PAGE_VALUE = "wiki-page";
    
    /**
     * Looks just like a CREATE, except pageTitle isn't set
     */
    @Override
    public boolean isEventFired(final HttpServletRequest request) {

        JSONObject jsonObject = parseJSONFromPostContent(request);
        if (jsonObject != null && !jsonObject.has(AlfrescoJSONKeys.CURRENT_VERSION)
                && jsonObject.has(AlfrescoJSONKeys.PAGE) && (!jsonObject.has(AlfrescoJSONKeys.PAGETITLE))) {
            try {
                return JSON_PAGE_VALUE.equals(jsonObject.getString(AlfrescoJSONKeys.PAGE));
            } catch (JSONException e) {
                logger.warn("Unable to parse JSON request.");
            }
        }

        return false;

    }

    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request, 
            final JSONObject json, final BufferedHttpServletResponse response) {
        setMetadataFromNodeRef(audit, nodeRefResolver.getNodeRef(request.getRequestURI()));
    }

}
