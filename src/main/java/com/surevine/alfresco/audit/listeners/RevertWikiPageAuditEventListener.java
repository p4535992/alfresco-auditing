package com.surevine.alfresco.audit.listeners;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.JSONException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * Audit listener for wiki page reversions.  This is awkward as a reversion is just an update to a content value that <i>happens</i> to be the 
 * same as a previous version - the conversation between share and alfresco (indeed, between the client and alfresco) on reverting wiki pages happens
 * without reference to version numbers(!).  There's therefore less information in this audit event than we would like.=, but it should still be sufficent.
 * @author simonw
 *
 */
public class RevertWikiPageAuditEventListener  extends PutAuditEventListener {

    /**
     * String literal to denote the type of action taken.
     */
    private static final String ACTION = "WIKI_PAGE_REVERTED";
    
    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(RevertWikiPageAuditEventListener.class);

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "slingshot/wiki/page/";

    /**
     * Default constructor which provides statics to the super class.
     */
    public RevertWikiPageAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }
    
    /**
     * Name expected of the page value in JSON.
     */
    public static final String JSON_PAGE_VALUE = "wiki-page";
    
    /**
     * Looks just like a CREATE, except pageTitle isn't set
     */
    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {

        JSONObject jsonObject = parseJSONFromPostContent(postContent);
        if (jsonObject != null && !jsonObject.has(AlfrescoJSONKeys.CURRENT_VERSION)
                && jsonObject.has(AlfrescoJSONKeys.PAGE) && (!jsonObject.has(AlfrescoJSONKeys.PAGETITLE))) {
            try {
                return JSON_PAGE_VALUE.equals(jsonObject.getString(AlfrescoJSONKeys.PAGE));
            } catch (JSONException e) {
                logger.warn("Unable to parse JSON request.");
            }
        }

        return false;

    }

    @Override
    public void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request, 
            final JSONObject json, final BufferedHttpServletResponse response) {
        setMetadataFromNodeRef(audit, nodeRefResolver.getNodeRef(request.getRequestURI()));
    }

}
