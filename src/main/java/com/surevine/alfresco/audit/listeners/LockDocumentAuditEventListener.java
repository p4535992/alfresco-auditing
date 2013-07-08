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

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class LockDocumentAuditEventListener extends PostAuditEventListener {

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "doclib/action/checkout";

    /**
     * Name of the event.
     */
    private static final String ACTION = "DOCUMENT_LOCKED";

    /**
     * Default constructor which provides statics to the super class.
     */
    public LockDocumentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    /** There are two possible sources for the action - the list view or the detailed view.
     * the presence of the string 'workspace/SpacesStore' suggests a noderef is present, otherwise the filename
     * is appended to the end of the URI.
     * @see com.surevine.alfresco.audit.listeners.PostAuditEventListener#setSpecificAuditMetadata(com.surevine.alfresco.audit.Auditable, javax.servlet.http.HttpServletRequest, org.json.JSONObject, com.surevine.alfresco.audit.BufferedHttpServletResponse)
     */
    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request, 
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
        setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRef(request.getRequestURI()));
    }
}
