package com.surevine.alfresco.audit.listeners;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.service.cmr.repository.NodeRef;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class RevertDocumentVersionAuditEventListener extends PostAuditEventListener {
    /**
     * String literal to denote the type of action taken.
     */
    private static final String ACTION = "REVERT_DOCUMENT";

    /**
     * Part of the URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "api/revert";

    /**
     * Default constructor which provides statics to the super class.
     */
    public RevertDocumentVersionAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject jsonObject, final BufferedHttpServletResponse response) throws JSONException {
        if (jsonObject != null && jsonObject.has(AlfrescoJSONKeys.NODEREF)) {
            setMetadataFromNodeRef(auditable, new NodeRef(jsonObject.getString(AlfrescoJSONKeys.NODEREF)));            
        }

    }

}
