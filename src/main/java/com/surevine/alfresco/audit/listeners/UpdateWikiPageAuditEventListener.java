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
public class UpdateWikiPageAuditEventListener extends PutAuditEventListener {

    /**
     * Part of URI that will be used to help identify event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/page/";

    /**
     * Name that will be persisted to identify action.
     */
    private static final String ACTION = "WIKI_PAGE_UPDATED";

    /**
     * Default constructor which provides statics to the super class.
     */
    public UpdateWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(UpdateWikiPageAuditEventListener.class);

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {

        JSONObject json = parseJSONFromPostContent(postContent);
        if (json != null) {
            if (json.has(AlfrescoJSONKeys.PAGE) && json.has(AlfrescoJSONKeys.CURRENT_VERSION)) {
                try {
                    return "wiki-page".equals(json.getString(AlfrescoJSONKeys.PAGE));
                } catch (JSONException e) {
                    logger.warn("Unable to parse json object");
                }
            }
        }

        return false;
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) {
        setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRef(request.getRequestURI()));
    }
}
