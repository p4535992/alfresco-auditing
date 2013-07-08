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
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {
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
