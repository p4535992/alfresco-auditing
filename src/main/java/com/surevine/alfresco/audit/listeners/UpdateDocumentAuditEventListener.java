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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;

/**
 * @author garethferrier
 * 
 */
public class UpdateDocumentAuditEventListener extends PostAuditEventListener {
    Logger logger = Logger.getLogger(UpdateDocumentAuditEventListener.class);
    
    /**
     * Part of the URI that distinguishes event.
     */
    public static final String URI_DESIGNATOR = "api/upload";
    
    /**
     * The string action which identifies the event.
     */
    public static final String ACTION = "DOCUMENT_UPDATED";

    /**
     * Default constructor which provides statics to the super class.
     */
    public UpdateDocumentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {
        return request.getRequestURI().contains(URI_DESIGNATOR)
            && postContent.contains("\"updateNodeRef\""
            + UploadDocumentAuditEventListener.MIME_LINE_DELIMITER
            + UploadDocumentAuditEventListener.MIME_LINE_DELIMITER + "workspace");
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) {
        parseFromMIMEData(auditable, request, json);
        
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        
        try {
            for (final Object o : upload.parseRequest(request)) {
                FileItem item = (FileItem) o;
                
                if (item.isFormField() && "tags".equals(item.getFieldName())) {
                    auditable.setTags(StringUtils.join(item.getString().trim().split(" "), ','));
                }
            }
        } catch (final FileUploadException e) {
            logger.error("Error while parsing request form", e);
        }
    }
}
