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

import javax.servlet.http.HttpServletRequest;

import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.alfresco.audit.BufferedServletOutputStream;

/**
 * @author garethferrier
 * 
 */
public class ViewDocumentDetailsAuditEventListener extends GetAuditEventListener {

    private static Log logger = LogFactory.getLog(ViewDocumentDetailsAuditEventListener.class);

    /**
     * Part of the URI that will be used to identify the event.
     */
    private static final String URI_DESIGNATOR = "api/metadata";

    /**
     * Name of event that is persisted.
     */
    private static final String ACTION = "VIEW_DOCUMENT_DETAILS";

    /**
     * Default constructor which provides statics to the super class.
     */
    public ViewDocumentDetailsAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request) throws JSONException {
        setMetadataFromNodeRef(audit, nodeRefResolver.getNodeRefFromGUID(request.getParameter(AlfrescoJSONKeys.NODEREF)));
    }

    @Override
    public boolean isEventFired(HttpServletRequest request, String postContent) {        
        return ((request.getRequestURI().contains(URI_DESIGNATOR)) && 
                request.getParameter("shortQNames") == null);
    }

    @Override
    public void decideSuccess(BufferedHttpServletResponse response, Auditable audit) throws JSONException, IOException {

        if (response != null) {
            byte[] failure = ((BufferedServletOutputStream) response.getOutputStream()).getOutputStreamAsByteArray();

            // There is a single failure scenario that needs to be dealt with here, this is when the response is
            // CRLF - which appears to be the case where a valid but unknown noderef is used or when a doc that 
            // is not available to the user, for permissions reasons, is requested.
            if (failure.length == 2) {
                
                byte firstByte = failure[0];
                byte secondByte = failure[1];
                
                if (firstByte == '\r' && secondByte == '\n') {
                    audit.setSuccess(false);
                    audit.setDetails("Invalid or unavailable resource requested");
                    logger.warn("Invalid or unreadable nodeRef details requested " + audit.getNodeRef());    
                }
                
            } else {
                // Just decide success as normal
                super.decideSuccess(response, audit);
            }
        }

    }
}
