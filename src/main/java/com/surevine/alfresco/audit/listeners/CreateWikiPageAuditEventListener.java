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

import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class CreateWikiPageAuditEventListener extends PutAuditEventListener {

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(CreateWikiPageAuditEventListener.class);

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/page/";

    /**
     * Name of the event.
     */
    private static final String ACTION = "WIKI_PAGE_CREATED";

    /**
     * Default constructor which provides statics to the super class.
     */
    public CreateWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    /**
     * Name expected of the page value in JSON.
     */
    public static final String JSON_PAGE_VALUE = "wiki-page";

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {

        JSONObject jsonObject = parseJSONFromPostContent(postContent);
        if (jsonObject != null && !jsonObject.has(AlfrescoJSONKeys.CURRENT_VERSION)
                && jsonObject.has(AlfrescoJSONKeys.PAGE) && jsonObject.has(AlfrescoJSONKeys.PAGETITLE)) {
            try {
                return JSON_PAGE_VALUE.equals(jsonObject.getString(AlfrescoJSONKeys.PAGE));
            } catch (JSONException e) {
                logger.warn("Unable to parse JSON request.");
            }
        }

        return false;

    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
        
        NodeRef nodeRef = nodeRefResolver.getNodeRef(request.getRequestURI());        
        setMetadataFromNodeRef(auditable, nodeRef);

    }
}
