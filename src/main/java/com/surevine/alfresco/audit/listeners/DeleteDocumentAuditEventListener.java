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
import org.alfresco.service.cmr.repository.NodeRef;

import com.surevine.alfresco.audit.Auditable;

/**
 * @author garethferrier
 * 
 */
public class DeleteDocumentAuditEventListener extends DeleteAuditEventListener {

    /**
     * Name of the event.
     */
    private static final String ACTION = "DOCUMENT_DELETED";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "doclib/action/file";

    /**
     * Default constructor which provides statics to the super class.
     */
    public DeleteDocumentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request) {
        boolean retVal = false;
        if (request.getRequestURI().contains(URI_DESIGNATOR)) {
            NodeRef ref = nodeRefResolver.getNodeRef(request.getRequestURI());
            if (ref != null) {
                retVal = ContentModel.TYPE_CONTENT.equals(getNodeService().getType(ref));
            }
        }

        return retVal;
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request) {
        NodeRef nodeRef = nodeRefResolver.getNodeRef(request.getRequestURI());
        setMetadataFromNodeRef(audit, nodeRef);
    }


}
