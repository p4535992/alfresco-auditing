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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.service.cmr.repository.NodeRef;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class CopyDocumentAuditEventListener extends PostAuditEventListener {

    /**
     * String literal for the action this listener finds.
     */
    private static final String ACTION = "COPY_DOCUMENT";

    /**
     * Part of the URI that identifies this event.
     */
    private static final String URI_DESIGNATOR = "doclib/action/copy-to";

    /**
     * Default constructor.
     */
    public CopyDocumentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject post, final BufferedHttpServletResponse response) throws JSONException {
        // TODO remove?
    }

    @Override
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response,
            final String postContent) throws JSONException {

        List<Auditable> events = new ArrayList<Auditable>();

        JSONObject json = parseJSONFromPostContent(postContent);

        String docLib = "documentLibrary";
        details = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(docLib) + docLib.length());
        
        if (json != null && json.has(AlfrescoJSONKeys.NODEREFS)) {
            JSONArray nodeRefs = json.getJSONArray(AlfrescoJSONKeys.NODEREFS);

            // Now iterate over each of the nodeRefs and create an audit item for each.
            for (int i = 0; i < nodeRefs.length(); i++) {
                String nodeStr = nodeRefs.getString(i);
                Auditable event = null;
                NodeRef nodeRef = null;
                if (nodeStr != null) {

                    nodeRef = new NodeRef(nodeStr);
                    if (getNodeService().exists(nodeRef)) {
                        event = new AuditItem();
                        setTags(event, json);
                        setGenericAuditMetadata(event, request);
                        setMetadataFromNodeRef(event, nodeRef);
                    }

                    // Now add the populated event to the array
                    events.add(event);
                }
            }
        }

        return events;
    }
    
    private String details;
    
    public String getDetails(NodeRef node) {
        return details;
    }
}
