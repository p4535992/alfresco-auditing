package com.surevine.alfresco.audit.listeners;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.esl.EnhancedSecurityLabel;

public abstract class PostFormAuditEventListener extends PostAuditEventListener {
    private static final Logger logger = Logger.getLogger(PostFormAuditEventListener.class);

    private static final String REQUEST_ATTRIBUTE_FORM = "com.surevine.alfresco.audit.FormData";
    
    protected Map<String, FileItem> formItems;

    public PostFormAuditEventListener(final String uriDesignator, final String action, final String method) {
        super(uriDesignator, action, method);
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request) {
        if (super.isEventFired(request) && ServletFileUpload.isMultipartContent(request)) {
            parseFormFromRequest(request);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void parseFormFromRequest(final HttpServletRequest request) {
        if(request.getAttribute(REQUEST_ATTRIBUTE_FORM) != null) {
            formItems = (Map<String, FileItem>) request.getAttribute(REQUEST_ATTRIBUTE_FORM);
        }
        
        formItems = new TreeMap<String, FileItem>(String.CASE_INSENSITIVE_ORDER);

        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");

        try {
            List<FileItem> mimeItems = upload.parseRequest(request);
            for (FileItem item : mimeItems) {
                formItems.put(item.getFieldName(), item);
            }
        } catch (FileUploadException e) {
            logger.error("Failure uploading file ", e);
            return;
        }
        
        request.setAttribute(REQUEST_ATTRIBUTE_FORM, formItems);
    }

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) {
        parseFromForm(auditable);
    }

    private void parseFromForm(final Auditable auditable) {
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

        for (FileItem item : formItems.values()) {
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
                // This is the key when flash is not used.
                tmpNationalityCaveats = item.getString();
            } else if (EnhancedSecurityLabel.ESL_EYES.equalsIgnoreCase(item.getFieldName())) {
                // This is the key when flash is used.
                tmpNationalityCaveats = item.getString();
            } else if (EnhancedSecurityLabel.CAVEATS_MIME_STR.equalsIgnoreCase(item.getFieldName())) {
                tmpCaveats = item.getString();
            } else if ("updateNodeRef".equalsIgnoreCase(item.getFieldName())) {
                tmpNodeRef = item.getString();
            } else if ("uploadDirectory".equalsIgnoreCase(item.getFieldName())) {
                tmpUploadDir = item.getString();
            } else if ("tags".equalsIgnoreCase(item.getFieldName())) {
                auditable.setTags(StringUtils.join(item.getString().trim().split(" "), ','));
            }
        }

        // Now try to construct the ESL
        // Note that this institutes a compile-time dependency on the ESL module, which we may wish to remove one day
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
                version = (String) getNodeService().getProperty(tmpNode, ContentModel.PROP_VERSION_LABEL);
            }
        }

        auditable.setVersion(version);

        auditable.setSecLabel(esl.toString());

        if (tmpUploadDir != null) {
            auditable.setDetails(tmpUploadDir);
        }
    }
}
