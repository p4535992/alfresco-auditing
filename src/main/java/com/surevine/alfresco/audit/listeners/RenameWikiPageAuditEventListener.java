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

import org.alfresco.model.ContentModel;
import org.alfresco.repo.version.Version2Model;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.util.ISO9075;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class RenameWikiPageAuditEventListener extends PostAuditEventListener {

    /**
     * Name of the action that will be persisted if this event fires.
     */
    private static final String ACTION = "WIKI_PAGE_RENAMED";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/page/";

    /**
     * Default constructor which provides statics to the super class.
     */
    public RenameWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }
    
    private static final Log logger = LogFactory.getLog(RenameWikiPageAuditEventListener.class);

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {

        JSONObject json = parseJSONFromPostContent(postContent);
        if (json != null) {
            return (json.has(AlfrescoJSONKeys.NAME) && json.has(AlfrescoJSONKeys.PAGE));
        }

        return false;
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
        
        NodeRef node = nodeRefResolver.getNodeRef(request.getRequestURI());
        setMetadataFromNodeRef(auditable, node);
        String newName = json.getString(AlfrescoJSONKeys.NAME);
       
        //Override the generic attempt to retrieve a version number (which will fail as the old page no longer exists)
        //by another method that gets the version of the new page and subtracts 0.1 from it
        //This method is risky depending on encodings and support non-critical functionality, so we log and swallow exceptions
        //rather than allowing them to percolate upward
        try
        {
            String nodePathStr = getNodeService().getPath(node).toString();
            String newNodePathStr = nodePathStr.substring(0, nodePathStr.lastIndexOf("}")+1).concat(ISO9075.encode(newName));
            logger.debug("Looking for a path:  "+newNodePathStr);
            NodeRef newNode = nodeRefResolver.getNodeRefFromPath(newNodePathStr, true);
            
            String version = (String) getNodeService().getProperty(newNode, ContentModel.PROP_VERSION_LABEL);
            if (version == null || version.equals("null") || version.trim().length() < 1) {
                version = (String) getNodeService().getProperty(node, Version2Model.PROP_QNAME_VERSION_LABEL);
            }
            if (version!=null)
            {
                //In Alfresco 3.4 and lower, we can assume that all wiki pages are numbered 1.X
                int newVersionNumber = Integer.parseInt(version.substring(2))-1;
                version = "1."+newVersionNumber;
                auditable.setVersion(version);
            }

        }
        catch (Exception e)
        {
            logger.warn("An exception occured when attempting to calculate the version number for a wiki page rename: "+e, e);
        }
        
        if (json.has(AlfrescoJSONKeys.NAME)) {
            auditable.setDetails("Renamed to " + newName);
        }
    }
}
