/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package com.surevine.alfresco.audit.listeners;

import javax.servlet.http.HttpServletRequest;

import com.surevine.alfresco.audit.Auditable;

/**
 * @author garethferrier
 * 
 */
public class DeleteDocumentCommentAuditEventListener extends DeleteAuditEventListener {

    /**
     * Name of the event.
     */
    private static final String ACTION = "COMMENT_ON_DOCUMENT_DELETED";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "api/comment/node/workspace";

    /**
     * Default constructor which provides statics to the super class.
     */
    public DeleteDocumentCommentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request) {
        
        audit.setNodeRef(nodeRefResolver.getNodeRef(request.getRequestURI()).toString());
        audit.setSource(request.getParameter("itemTitle"));
        audit.setSite(request.getParameter("site"));
        audit.setSecLabel(NO_SECURITY_LABEL);
        audit.setVersion(NO_VERSION_STRING);
    }


}
