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
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * @author garethferrier
 * 
 */
public class CreateFolderAuditEventListener extends PostAuditEventListener {

    /**
     * Name of the event.
     */
    private static final String ACTION = "FOLDER_CREATED";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "cm_folder/formprocessor";

    /**
     * Default constructor provides statics to the super class.
     */
    public CreateFolderAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request) {
        return request.getRequestURI().contains(URI_DESIGNATOR);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
        if (json.has(AlfrescoJSONKeys.CM_FOLDER_NAME)) {
            auditable.setSource(json.getString(AlfrescoJSONKeys.CM_FOLDER_NAME));
        }
        
        if (json.has(AlfrescoJSONKeys.DEST_FOLDER)) {
            auditable.setDetails(json.getString(AlfrescoJSONKeys.DEST_FOLDER));
            
            // We can use the destination to get at the site
            auditable.setSite(nodeRefResolver.getSiteName(new NodeRef(json.getString(AlfrescoJSONKeys.DEST_FOLDER))));
        }
        
        auditable.setNodeRef(NODE_UNAVAILABLE);
        auditable.setSecLabel(NO_SECURITY_LABEL);
        auditable.setVersion(NO_VERSION_STRING);
    }
}
