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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.service.cmr.model.FileNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

public class MarkForDeleteAuditEventListener extends PostAuditEventListener {

    /**
     * Part of the URI used to identify the event.
     */
    public static final String URI_DESIGNATOR = "sv-theme/delete/markForDelete";

    /**
     * Name of the event.
     */
    public static final String ACTION = "MARKED_FOR_DELETE";

    private static final Log logger = LogFactory.getLog(MarkForDeleteAuditEventListener.class);

    /**
     * Default constructor which provides statics to the super class.
     */
    public MarkForDeleteAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {

        final String pathParam = request.getParameter("path");
        final String[] nodeRefs = request.getParameterValues(AlfrescoJSONKeys.NODEREF);
        
        if (pathParam != null) {
            // If the path parameter is non null then we resovle the nodeRef using the path.
            try {
                setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRefFromPath(pathParam));
            } catch (FileNotFoundException fne) {
                logger.warn("Could not find file for path: " + pathParam, fne);
            }
        } else if (nodeRefs.length == 0) {
            throw new RuntimeException("Expected either a path or nodeRef parameter. None found.");
        } else {
            setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRefFromGUID(nodeRefs[0]));
        }
    }

    @Override
    protected void populateSecondaryAuditItems(List<Auditable> events, HttpServletRequest request,
            HttpServletResponse response, JSONObject postContent) throws JSONException {
        final String[] nodeRefs = request.getParameterValues(AlfrescoJSONKeys.NODEREF);
        
        if (request.getParameter("path") == null && nodeRefs.length > 1) {
            for (int i = 1; i<nodeRefs.length; i++) {
                Auditable primaryEvent = events.get(0);
                
                AuditItem item = new AuditItem();
                
                setGenericAuditMetadata(item, request);
                
                item.setAction(ACTION);
                
                setMetadataFromNodeRef(item, nodeRefResolver.getNodeRefFromGUID(nodeRefs[i]));
                item.setDetails(primaryEvent.getDetails());
                
                events.add(item);
            }
        }
    }
}
