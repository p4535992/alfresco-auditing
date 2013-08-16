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

import javax.servlet.http.HttpServletRequest;

import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.alfresco.audit.BufferedServletOutputStream;

/**
 * This listener is a little more interesting than most (interesting being a relative term).  On the request only the topic, or 
 * previous reply is quoted, as the actual node has not been created yet.  The created nodeRef is populated in the response 
 * JSON, hence we need to dive into it to resolve the nodeRef.  Although all of the information for population in the audit log
 * is available in the response JSON use the preferred method of making service calls, rather than parsing it all out from the 
 * JSON.
 * 
 * @author garethferrier
 * 
 */
public class CreateDiscussionReplyAuditEventListener extends PostAuditEventListener {


    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(CreateDiscussionReplyAuditEventListener.class);
    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "forum/post/";

    /**
     * Name of the event.
     */
    private static final String ACTION = "DISCUSSION_REPLY";

    /**
     * Default constructor provides statics to the super class.
     */
    public CreateDiscussionReplyAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {
        return request.getRequestURI().contains(URI_DESIGNATOR) && request.getRequestURI().contains("replies");
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject postJSON, final BufferedHttpServletResponse response) throws JSONException {
        byte[] responseBytes;
        try {
            responseBytes = ((BufferedServletOutputStream) response.getOutputStream()).getOutputStreamAsByteArray();
            
            String jsonResponseString = new String(responseBytes, "UTF-8");
            JSONObject responseJSON = new JSONObject(jsonResponseString);
            String nodeRefStr = responseJSON.getJSONObject("item").getString(AlfrescoJSONKeys.NODEREF);

            setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRefFromGUID(nodeRefStr));
        } catch (IOException e) {
            logger.error("Failure parsing JSON from response object");
            throw new JSONException(e);
        }

    }
}
