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
public class UpdateDocumentPermissionsAuditEventListener extends PostAuditEventListener {

    /**
     * Element of the URI that is used to help identify the event.
     */
    private static final String URI_DESIGNATOR = "doclib/action/permissions";

    /**
     * Action that persisted to denote the event.
     */
    private static final String ACTION = "DOCUMENT_PERMISSIONS_UPDATED";

    /**
     * Default constructor which provides statics to the super class.
     */
    public UpdateDocumentPermissionsAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
        // TODO Empty implementation needs refactored.
    }

    @Override
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response,
            final String postContent) throws JSONException {

        List<Auditable> events = new ArrayList<Auditable>();

        JSONObject json = parseJSONFromPostContent(postContent);

        if (json != null && json.has(AlfrescoJSONKeys.NODEREFS)) {
            JSONArray arr = json.getJSONArray(AlfrescoJSONKeys.NODEREFS);

            for (int i = 0; i < arr.length(); i++) {

                String nodeStr = arr.getString(i);
                if (nodeStr != null) {
                    Auditable event = new AuditItem();
                    setGenericAuditMetadata(event, request);
                    setMetadataFromNodeRef(event, nodeRefResolver.getNodeRefFromGUID(nodeStr));
                    events.add(event);
                }
            }
        }

        return events;
    }

}
