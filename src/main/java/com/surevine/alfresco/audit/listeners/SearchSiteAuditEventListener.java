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

import com.surevine.alfresco.audit.Auditable;

/**
 * @author garethferrier
 * 
 */
public class SearchSiteAuditEventListener extends GetAuditEventListener {

    /**
     * Name of the event.
     */
    private static final String ACTION = "SEARCH";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "slingshot/search";

    /**
     * Default constructor which provides statics to the super class.
     */
    public SearchSiteAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request) {
        audit.setSite(request.getParameter("site"));
        audit.setSource(request.getParameter("term"));
        audit.setSecLabel("No label for search.");
        audit.setVersion(NO_VERSION_STRING);
        audit.setNodeRef(NODE_UNAVAILABLE);
    }

}
