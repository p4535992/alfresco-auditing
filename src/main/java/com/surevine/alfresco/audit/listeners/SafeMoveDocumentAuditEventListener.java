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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.repository.StoreRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.alfresco.audit.BufferedServletOutputStream;

/**
 * @author jonnyh
 * 
 */
public class SafeMoveDocumentAuditEventListener extends PostAuditEventListener {

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(SafeMoveDocumentAuditEventListener.class);

    /**
     * Move needs it own implementation as a multi-document can fail for a number of reasons, but will respond with a
     * 200 status (HttpServletResponse.SC_OK), which should obviously not be reported.
     * 
     * @throws JSONException
     * @throws IOException
     * @see com.surevine.alfresco.audit.listeners.AbstractAuditEventListener#decideSuccess(com.surevine.alfresco.audit.BufferedHttpServletResponse,
     *      com.surevine.alfresco.audit.Auditable)
     */
    @Override
    public void decideSuccess(BufferedHttpServletResponse response, Auditable audit) throws JSONException, IOException {

        if (response != null) {
            int responseStatus = response.getStatus();
            JSONObject failureJSON;
            try {
                byte[] failure = ((BufferedServletOutputStream) response.getOutputStream())
                        .getOutputStreamAsByteArray();
                if (failure.length > 0) {
                    byte firstByte = failure[0];
                    if (firstByte == '{') {
                        // Then it is a JSON string or very likely, continue with parsing.
                        String responseMessage = new String(failure, "UTF-8");
                        failureJSON = new JSONObject(responseMessage);
                        if (failureJSON.has(AlfrescoJSONKeys.OVERALL_SUCCESS)) {
                            // Now only return content if the value of overall success is false.
                            if ("false".equals(failureJSON.getString(AlfrescoJSONKeys.OVERALL_SUCCESS))) {
                                audit.setDetails(responseStatus + ":false");
                                audit.setSuccess(false);
                            }

                        }
                    }

                }
            } catch (JSONException e) {
                logger.warn("Unable to parse failure JSON definition", e);
                throw (e);
            }
        }

    }

    private static final String DOCUMENT_LIBRARY = "documentLibrary";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "doclib/action/safe-move-to";

    /**
     * Name of the event.
     */
    private static final String ACTION = "SAFE_MOVE_DOCUMENT";

    /**
     * Default constructor which provides statics to the super class.
     */
    public SafeMoveDocumentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {

        // No implementation here, refactored out to accomodate multiples.
    }

    @Override
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response) throws JSONException {

        List<Auditable> events = new ArrayList<Auditable>();

        JSONObject json = parseJSONFromPostContent(request);

        String destFolderNodeId = request.getRequestURI().substring(
                request.getRequestURI().lastIndexOf("/") + 1);
        
        NodeRef destFolderNodeRef = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE + "/" + destFolderNodeId);
        
        NodeService _nodeService = getNodeService();

        Path destFolderPath = _nodeService.getPath(destFolderNodeRef);

        if (json != null && json.has(AlfrescoJSONKeys.NODEREFS)) {
            JSONArray nodeRefs = json.getJSONArray(AlfrescoJSONKeys.NODEREFS);

            // Now iterate over each of the nodeRefs and create an audit item for each.
            for (int i = 0; i < nodeRefs.length(); i++) {
                String nodeStr = nodeRefs.getString(i);

                if (nodeStr != null) {
                    Auditable event = new AuditItem();
                    setGenericAuditMetadata(event, request);

                    setMetadataFromNodeRef(event, nodeRefResolver.getNodeRefFromGUID(nodeStr));
                    event.setDetails(destFolderPath.toString());
                    setTags(event, json);
                    // Now add the populated event to the array
                    events.add(event);
                }
            }
        }

        return events;
    }
}
