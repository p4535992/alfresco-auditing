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
public class UpdateWikiPageAuditEventListener extends PutAuditEventListener {

    /**
     * Part of URI that will be used to help identify event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/page/";

    /**
     * Name that will be persisted to identify action.
     */
    private static final String ACTION = "WIKI_PAGE_UPDATED";

    /**
     * Default constructor which provides statics to the super class.
     */
    public UpdateWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(UpdateWikiPageAuditEventListener.class);

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {

        JSONObject json = parseJSONFromPostContent(postContent);
        if (json != null) {
            if (json.has(AlfrescoJSONKeys.PAGE) && json.has(AlfrescoJSONKeys.CURRENT_VERSION)) {
                try {
                    return "wiki-page".equals(json.getString(AlfrescoJSONKeys.PAGE));
                } catch (JSONException e) {
                    logger.warn("Unable to parse json object");
                }
            }
        }

        return false;
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) {
        setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRef(request.getRequestURI()));
    }
}
