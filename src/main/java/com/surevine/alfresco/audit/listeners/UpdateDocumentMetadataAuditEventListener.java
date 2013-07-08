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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class UpdateDocumentMetadataAuditEventListener extends PostAuditEventListener {

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(UpdateDocumentMetadataAuditEventListener.class);

    /**
     * String persisted to identify event.
     */
    private static final String ACTION = "DOCUMENT_METADATA_UPDATED";

    /**
     * Part of URI used to help identify event.
     */
    private static final String URI_DESIGNATOR = "api/node/workspace";

    private static final String URI_DESIGNATOR_SECONDARY = "api/metadata/node";
    
    /**
     * There are two actions that have different URI designators to change metadata.
     * Until the API changes to reflect the multiplicity keep this within this class.
     */
    private static final String[] URI_DESIGNATORS = {URI_DESIGNATOR, URI_DESIGNATOR_SECONDARY};
    
    /**
     * Default constructor which provides statics to the super class.
     */
    public UpdateDocumentMetadataAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException {
        setMetadataFromNodeRef(auditable, nodeRefResolver.getNodeRef(request.getRequestURI()));

        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        
        final StringBuilder postContent = new StringBuilder();
        String line;
        try {
            final BufferedReader br = request.getReader();
            while ((line = br.readLine()) != null) {
                postContent.append(line);
            }
        } catch (final IOException e) {
            logger.error("Failed to update audit record with tags.", e);
        }
        
        if (postContent.length() > 0) {
            final JSONObject jsonContent = new JSONObject(postContent.toString());
            
            if (jsonContent.has("prop_cm_taggable")) {
                final String[] tags = jsonContent.getString("prop_cm_taggable").split(",");
                final List<String> tagList = new ArrayList<String>(tags.length);
                
                final NodeService nodeService = getNodeService();
                for (final String tag : tags) {
                    final NodeRef nodeRef = nodeRefResolver.getNodeRefFromGUID(tag);
                    final String name = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
                    
                    tagList.add(name);
                }
                
                auditable.setTags(StringUtils.join(tagList.iterator(), ","));
            }
        }
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {

        for (String desig : URI_DESIGNATORS) {
            if (request.getRequestURI().contains(desig)) {
                return true;
            }
        }
        
        return false;
    }

}
