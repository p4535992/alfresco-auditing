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
        // Intentionally do nothing
    }

    @Override
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response) throws JSONException {

        List<Auditable> events = new ArrayList<Auditable>();

        JSONObject json = parseJSONFromPostContent(request);

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
