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

import javax.servlet.http.HttpServletRequest;

import org.alfresco.service.cmr.repository.NodeRef;

import com.surevine.alfresco.audit.Auditable;
/**
 * @author garethferrier
 *
 */
public class ViewWikiPageAuditEventListener extends GetAuditEventListener {

    /**
     * Part of the URI that will be used to identify the event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/";
    
    
    /**
     * Don't want a false hit against accessing the wiki page list which has the same start.
     */
    private static final String WIKI_PAGE_LIST_ACCESS_DESIGNATOR = URI_DESIGNATOR + "pages"; 
    
    /**
     * Name that will be persisted to identify event.
     */
    public static final String ACTION = "VIEW_WIKI_PAGE";

    /**
     * Default constructor which provides statics to the super class.
     */
    public ViewWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    /**
     * Two things need to be set as part of setting the specific metadata -
     * 
     * 1. Get the source - the page that is being viewed from the requestURI.
     * 2. Get the site from the requestURI also.
     * 
     * @see com.surevine.alfresco.audit.listeners.GetAuditEventListener#setSpecificAuditMetadata(com.surevine.alfresco.audit.Auditable, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request) {
        setMetadataFromNodeRef(audit, nodeRefResolver.getNodeRef(request.getRequestURI()));
    }

    @Override
    public boolean isEventFired(HttpServletRequest request, String postContent) {

        //Need to be wary of the scenario where the wiki page list is accessed
        if (request.getRequestURI().contains(WIKI_PAGE_LIST_ACCESS_DESIGNATOR) ) {
            return false;
        } else {
            return super.isEventFired(request, postContent);    
        }
        
    }


}
