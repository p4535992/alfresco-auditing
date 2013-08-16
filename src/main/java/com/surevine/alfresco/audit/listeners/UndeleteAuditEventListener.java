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
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.alfresco.audit.NodeRefResolver;

public class UndeleteAuditEventListener extends PutAuditEventListener {

    /**
     * Part of the URI used to identify the event.
     */
    public static final String URI_DESIGNATOR = "sv-theme/delete/undelete";

    /**
     * Name of the event.
     */
    public static final String ACTION = "UNDELETED";

    private static final Log logger = LogFactory.getLog(RemoveDeletionMarkAuditEventListener.class);
    
    /**
     * Default constructor which provides statics to the super class.
     */
    public UndeleteAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request, 
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
            
        String pathParam = request.getParameter("path");
        if (pathParam!=null)
        {
            try
            {
                setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRefFromPath(pathParam));
            }
            catch (FileNotFoundException fne)
            {
                logger.warn("Could not find file for path: "+pathParam, fne);
            }
        }
        else
        {
            setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRefFromGUID(request.getParameter(AlfrescoJSONKeys.NODEREF)));
        }
        
    }
}
