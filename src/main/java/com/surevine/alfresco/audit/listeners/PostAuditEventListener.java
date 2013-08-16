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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * This is the abstract event listener for http POST methods. It implements parsing of post data from the http request
 * object.
 * 
 * @author garethferrier
 * 
 */
public abstract class PostAuditEventListener extends AbstractAuditEventListener {
    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(PostAuditEventListener.class);


    public PostAuditEventListener(final String uriDesignator, final String action, final String method) {
        super(uriDesignator, action, method);
    }

    /**
     * Method that this listener implements - POST.
     */
    public static final String METHOD = "POST";

    /**
     * String that a valid JSON string starts with.
     */
    public static final String JSON_START_STRING = "{";

    /**
     * {@inheritDoc}
     * 
     * @throws JSONException
     */
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response,
            final String postContent) throws JSONException {

        Auditable toAudit = new AuditItem();
        JSONObject postContentJSONObject = parseJSONFromPostContent(postContent);
        if (postContentJSONObject != null) {
            setSecurityLabel(toAudit, postContentJSONObject);
            setTags(toAudit, postContentJSONObject);
        }
        
        setSpecificAuditMetadata(toAudit, request, postContentJSONObject, (BufferedHttpServletResponse) response);
        setGenericAuditMetadata(toAudit, request);

        List<Auditable> items = new ArrayList<Auditable>();
        items.add(toAudit);

        populateSecondaryAuditItems(items, request, response, postContentJSONObject);
        
        return items;
    }
    
    /**
     * Method which can be overridden my listeners which may wish to add extra audit events based on posted data
     * 
     * @param events the list of {@link Auditable} objects to which to add extra events
     * @param request
     * @param response
     * @param postContent the posted json
     * @throws JSONException 
     */
    protected void populateSecondaryAuditItems(final List<Auditable> events, final HttpServletRequest request,
            final HttpServletResponse response, final JSONObject postContent) throws JSONException {
        // Do nothing by default
    }

    /**
     * Assumes that the post content was a JSON string.
     * 
     * @param postContent
     *            from the request
     * @return valid JSONObject, otherwise null
     */
    public static JSONObject parseJSONFromPostContent(final String postContent) {

        JSONObject retVal = null;
        if (postContent != null && !"".equals(postContent) && postContent.startsWith(JSON_START_STRING)) {
            try {
                retVal = new JSONObject(postContent);
            } catch (JSONException e) {
                logger.warn("Invalid JSON string parsed from post content ", e);
            }
        }

        return retVal;

    }

    /**
     * Utility method to set the tags (if changed) on a auditable item.
     * 
     * @param toAudit
     *            the event
     * @param jsonObject
     *            containing the tags
     * @throws JSONException
     */
    protected void setTags(final Auditable toAudit, final JSONObject jsonObject) {
        if (jsonObject.has(AlfrescoJSONKeys.TAGS)) {
            Object obj;
            try {
                obj = jsonObject.get(AlfrescoJSONKeys.TAGS);
                if (obj instanceof JSONArray) {
                    if (jsonObject.has(AlfrescoJSONKeys.TAGS)) {
                        // Iterate over the tags and add to the auditable item.
                        JSONArray tags = jsonObject.getJSONArray(AlfrescoJSONKeys.TAGS);
                        StringBuilder buf = new StringBuilder();

                        for (int i = 0; i < tags.length(); i++) {
                            buf.append(tags.get(i).toString());

                            if (i != tags.length()) {
                                buf.append(", ");
                            }
                        }
                        toAudit.setTags(buf.toString());
                    }
                }
            } catch (JSONException e) {
                logger.warn("JSONException found parsing tags ", e);
            }

        }
    }

    /**
     * Utility method to set the security label on an audit item based on json.
     * 
     * @param toAudit
     * @param jsonObject
     */
    protected void setSecurityLabel(final Auditable toAudit, final JSONObject jsonObject) {
        EnhancedSecurityLabel esl;
        try {
            esl = EnhancedSecurityLabel.buildLabel(jsonObject);
            if (esl != null) {
                toAudit.setSecLabel(esl.toString());
            }
        } catch (JSONException e) {
            // There was no security label in the JSON - warn only.
            logger.warn("Unable to parse security label from JSON input");
        }
    }

    /**
     * @param auditable
     * @param request
     * @param json
     * @param responseJSONObject
     * @throws JSONException
     */
    public abstract void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException;

    /**
     * Default implementation. Can be used by some listeners.
     * 
     * @param request
     *            the servlet request
     * @param postContent
     *            post data if present
     * @return boolean whether an audit event is fired.
     */
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {
        return request.getRequestURI().contains(getURIDesignator());
    }


    @SuppressWarnings("unchecked")
    protected void parseFromMIMEData(final Auditable auditable, final HttpServletRequest request, final JSONObject json) {
        if (ServletFileUpload.isMultipartContent(request)) {

            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            upload.setHeaderEncoding("UTF-8");
            // Parse the request
            String tmpProtectiveMarking = null;
            String tmpNationalOwner = null;
            String tmpNationalityCaveats = null;
            String tmpCaveats = null;
            String tmpAtomal = null;
            String tmpClosedGroups = null;
            String tmpOpenGroups = null;
            String tmpOrganisations = null;
            String tmpNodeRef = null;
            String tmpUploadDir = null;
            List<FileItem> mimeItems;

            try {
                mimeItems = upload.parseRequest(request);
                for (FileItem item : mimeItems) {
                    if (!item.isFormField()) {
                        auditable.setSource(item.getName());
                    } else if ("siteId".equals(item.getFieldName())) {
                        auditable.setSite(item.getString());
                    } else if (EnhancedSecurityLabel.OPEN_GROUPS_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpOpenGroups = item.getString();
                    } else if (EnhancedSecurityLabel.CLOSED_GROUPS_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpClosedGroups = item.getString();
                    } else if (EnhancedSecurityLabel.ORGANISATIONS_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpOrganisations = item.getString();
                    } else if (EnhancedSecurityLabel.ATOMAL_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpAtomal = item.getString();
                    } else if (EnhancedSecurityLabel.PROTECTIVE_MARKING_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpProtectiveMarking = item.getString();
                    } else if (EnhancedSecurityLabel.NATIONALITY_OWNER_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpNationalOwner = item.getString();
                    } else if (EnhancedSecurityLabel.NATIONAL_CAVEATS_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        // TODO this is the key when flash is not used.
                        tmpNationalityCaveats = item.getString();
                    } else if (EnhancedSecurityLabel.ESL_EYES.equalsIgnoreCase(item.getFieldName())) {
                        // TODO This is the key when flash is used.
                        tmpNationalityCaveats = item.getString();
                    } else if (EnhancedSecurityLabel.CAVEATS_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                        tmpCaveats = item.getString();
                    } else if ("updateNodeRef".equalsIgnoreCase(item.getFieldName())) {
                        tmpNodeRef = item.getString();
                    } else if ("uploadDirectory".equalsIgnoreCase(item.getFieldName())) {
                        tmpUploadDir = item.getString();
                    }
                }
            } catch (FileUploadException e) {
                logger.error("Failure uploading file ", e);
                return;
            }

            // Now try to construct the ESL
            // TODO move this into the ESL code.
            EnhancedSecurityLabel esl = new EnhancedSecurityLabel(tmpProtectiveMarking);
            esl.setNationalityOwner(tmpNationalOwner);
            esl.setNationalityCaveats(tmpNationalityCaveats);
            esl.setCaveat(tmpCaveats);
            String[] groupSet;

            if (tmpOpenGroups != null && !"".equals(tmpOpenGroups)) {
                groupSet = tmpOpenGroups.split(EnhancedSecurityLabel.DELIMITER);

                for (String set : groupSet) {
                    esl.addOpenGroup(set);
                }
            }

            if (tmpClosedGroups != null && !"".equals(tmpClosedGroups)) {
                groupSet = tmpClosedGroups.split(EnhancedSecurityLabel.DELIMITER);

                for (String set : groupSet) {
                    esl.addClosedGroup(set);
                }
            }
            
            if (tmpOrganisations != null && !"".equals(tmpOrganisations)) {
                groupSet = tmpOrganisations.split(EnhancedSecurityLabel.DELIMITER);

                for (String set : groupSet) {
                    esl.addOrganisation(set);
                }
            }

            if (tmpAtomal != null && !"".equals(tmpAtomal)) {
                groupSet = tmpAtomal.split(EnhancedSecurityLabel.DELIMITER);

                for (String set : groupSet) {
                    esl.addAtomal(set);
                }
            }

            // If the tmpNodeRef is null (for first time upload) then set to a default string
            if (tmpNodeRef == null || tmpNodeRef.equals("")) {
                tmpNodeRef = NODE_UNAVAILABLE;
            }
            
            auditable.setNodeRef(tmpNodeRef);

            // Default to the initial version
            String version = INITIAL_VERSION;
            
            // It's a little more involved to get the version as we need to use the noderef to resolve the
            // node itself and then get the label.
            if (tmpNodeRef != null && NodeRef.isNodeRef(tmpNodeRef)) {
                NodeRef tmpNode = new NodeRef(tmpNodeRef);

                if (getNodeService().exists(tmpNode)) {
                    version = (String) getNodeService()
                            .getProperty(tmpNode, ContentModel.PROP_VERSION_LABEL);
                }
            }

            auditable.setVersion(version);

            auditable.setSecLabel(esl.toString());

            if (tmpUploadDir != null) {
                auditable.setDetails(tmpUploadDir);
            }

        }

    }

}
