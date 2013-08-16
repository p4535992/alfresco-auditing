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
package com.surevine.alfresco.audit.integration.stubs;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.site.SiteService;

import com.surevine.alfresco.audit.NodeRefResolver;
import static com.surevine.alfresco.audit.integration.AbstractAuditIntegrationTestBase.*;

public class StubNodeRefResolver implements NodeRefResolver {

    /*
     * (non-Javadoc)
     * 
     * @see com.surevine.alfresco.audit.NodeRefResolver#getNodeRefFromPath(java.lang.String)
     */
    public NodeRef getNodeRefFromPath(final String path) throws FileNotFoundException {
        return getDummyNodeRef(path);
    }

    @Override
    public NodeRef getNodeRefFromPath(Path path) throws FileNotFoundException {
        return this.getNodeRefFromPath(path.toString());
    }
    
    @Override
    public String getShortPathString(Path path) {
        return path.toString();
    }
    
    private NodeService nodeService;

    @Override
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
        
    }
    
    private NodeRef getDummyNodeRef(String requestURI) {
        if (requestURI.contains(TEST_FOLDER)  || requestURI.contains(TEST_FOLDERNODEREF_ID)) {
            return new NodeRef(TEST_FOLDERNODEREF_STRING);
        } else if (requestURI.contains(TEST_FOLDERNODEREF_STRING)) {
            return new NodeRef(TEST_FOLDERNODEREF_STRING);
        } else if (requestURI.contains(TEST_FILE)) {
            return new NodeRef(TEST_FILENODEREF_STRING);
        } else if (requestURI.contains("/app:company_home")) {
            return new NodeRef(TEST_FILENODEREF_STRING);
        } else if (requestURI.contains(TEST_VERSIONED_WIKIPAGE_NODEREF_ID)) {
            return new NodeRef(TEST_VERSIONED_WIKIPAGE_NODEREF_STRING);
        } else if (requestURI.contains("wiki")) {
            return new NodeRef(TEST_WIKIPAGE_NODEREF_STRING);
        } else if (requestURI.contains("discussions")) {
            return new NodeRef(TEST_DISCUSSION_TOPIC_NODEREF_STRING);
        } else if (requestURI.contains(TEST_DISCUSSION_REPLY_NODEREF_ID)) {
            return new NodeRef(TEST_DISCUSSION_REPLY_NODEREF_STRING);
        } else {
            return new NodeRef(TEST_FILENODEREF_STRING);
        }

                
    }

    @Override
    public String getSiteName(NodeRef node) {
        return TEST_SITE;
    }

    @Override
    public NodeRef getNodeRefFromGUID(String nodeRefStr) throws AlfrescoRuntimeException {
        return getDummyNodeRef(nodeRefStr);
    }

    @Override
    public NodeRef getNodeRefFromPath(String path, boolean useFullPath) throws AlfrescoRuntimeException,
            FileNotFoundException {
        return getNodeRefFromPath(path);
    }

    @Override
    public NodeRef getNodeRef(String nodeString) throws AlfrescoRuntimeException {
        return getDummyNodeRef(nodeString);
    }

}
