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

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.version.Version2Model;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.alfresco.audit.NodeRefResolver;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * @author garethferrier
 * 
 */
public abstract class AbstractAuditEventListener implements AuditEventListener {

    public static final String SECURITY_LABEL_DELIMITER = ", ";
    
    public static final String NO_VERSION_STRING = "NONE";

    public static final String NODE_UNAVAILABLE = "UNAVAILABLE";
    
    public static final String NO_SECURITY_LABEL = "NO SECURITY LABEL APPLIED";

    private String uriDesignator;

    public String getURIDesignator() {
        return this.uriDesignator;
    }

    private String action;

    public String getAction() {
        return this.action;
    }

    private String method;

    private NodeService nodeService;

    /**
     * Resolves paths to node refs
     */
    NodeRefResolver nodeRefResolver;

    public void setNodeRefResolver(NodeRefResolver nodeRefResolver) {
        this.nodeRefResolver = nodeRefResolver;
    }

    public String getMethod() {
        return this.method;
    }

    /**
     * Constructor. Initialise each of the designator, action and method.
     * 
     * @param uriDesignator
     *            unique part of uri
     * @param action
     *            name of the action
     * @param method
     *            of the type used.
     */
    public AbstractAuditEventListener(final String uriDesignator, final String action, final String method) {
        this.uriDesignator = uriDesignator;
        this.action = action;
        this.method = method;
    }

    /**
     * @param auditable
     *            the event
     * @param request
     *            http
     */
    public void setGenericAuditMetadata(final Auditable auditable, final HttpServletRequest request) {
        auditable.setAction(getAction());
        auditable.setUrl(request.getRequestURI());
        auditable.setRemoteAddress(request.getRemoteAddr());
    }

    /**
     * For now just return the path of the node - will be specialized in subclasses where appropriate.
     * 
     * @param nodeRef
     * @return
     */
    protected String getDetails(NodeRef nodeRef) {
        return getNodeService().getPath(nodeRef).toString();
    }

    /**
     * Default implementation, uses the response code to make an assertion on whether success is achieved.
     * 
     * @throws JSONException
     * @throws IOException
     * 
     * @see com.surevine.alfresco.audit.listeners.AuditEventListener#decideSuccess(com.surevine.alfresco.audit.BufferedHttpServletResponse,
     *      com.surevine.alfresco.audit.Auditable)
     */
    public void decideSuccess(final BufferedHttpServletResponse response, final Auditable audit) throws JSONException,
            IOException {

        HttpStatus responseStatus = HttpStatus.valueOf(response.getStatus());

        // Use the enumeration of the type to decide whether or not a failure response should be signalled.
        switch (responseStatus.series()) {
        case CLIENT_ERROR:
        case SERVER_ERROR:
            audit.setSuccess(false);
            audit.setDetails(responseStatus.value() + ": " + responseStatus.name());
            break;
        default:
            audit.setSuccess(true);
            break;
        }
    }

    /**
     * The security label properties are persisted in the repository get the string definition of the node ref.
     * 
     * @param nodeRef
     *            to get the security properties for
     * @return String defintion of the
     */
    @SuppressWarnings("unchecked")
    protected String extractSecurityLabelFromRepo(final NodeRef nodeRef) {
        
        Map<QName, Serializable> properties  = nodeService.getProperties(nodeRef);

        EnhancedSecurityLabel esl = null;
        
        // Check to see if the resource is in the version store, because if it is then there will be no security label.
        if (isNodeInVersionStore(nodeRef)) {
            return NO_SECURITY_LABEL;
        }
        
        
        try {
            esl = new EnhancedSecurityLabel(properties);
        } catch(Exception e) {
            // THis will be the case where there was no valid security label in the properties.
            return NO_SECURITY_LABEL;
        }

        return esl.toString();
    }

    /**
     * @param nodeRef
     * @return true if the nodeRef is in the version store.
     */
    private boolean isNodeInVersionStore(NodeRef nodeRef) {
        
        StoreRef versionStore = new StoreRef("versionStore", "version2Store");
        return nodeRef.getStoreRef().equals(versionStore);
    }

    /**
     * Spring available getter.
     * 
     * @return nodeService
     */
    public NodeService getNodeService() {
        return nodeService;
    }

    /**
     * Spring available setter.
     * 
     * @param nodeService
     *            inside repo.
     */
    public void setNodeService(final NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public static final String INITIAL_VERSION = "1.0";

    protected void setMetadataFromNodeRef(Auditable auditable, NodeRef node) {

        if (auditable == null || node == null || !getNodeService().exists(node)) {
            throw new IllegalArgumentException("Invalid argument supplied to setMetadataFromNodeRef()");
        } else {
            auditable.setSource((String) getNodeService().getProperty(node, ContentModel.PROP_NAME));
            
            // Should only attempt to retrieve the nodes site ID if its not in a version store.
            if (!isNodeInVersionStore(node)) {
                auditable.setSite(nodeRefResolver.getSiteName(node));
            }
            
            auditable.setNodeRef(node.toString());

            if (ContentModel.TYPE_FOLDER.equals(getNodeService().getType(node))) {
                auditable.setSecLabel(NO_SECURITY_LABEL);
            } else {
                auditable.setSecLabel(extractSecurityLabelFromRepo(node));
            }

            auditable.setDetails(getDetails(node));
            
            // In the future, we may wish to resolve version numbering at a specific type.
            String version = (String) getNodeService().getProperty(node, ContentModel.PROP_VERSION_LABEL);
            if (version == null || version.equals("null") || version.trim().length() < 1) {
                version = (String) getNodeService().getProperty(node, Version2Model.PROP_QNAME_VERSION_LABEL);

                // Last resort, set it statically.
                if (version == null) {
                    version = NO_VERSION_STRING;
                }
            }

            auditable.setVersion(version);
        }
    }

}
