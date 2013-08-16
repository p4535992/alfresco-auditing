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
package com.surevine.alfresco.audit;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.site.SiteService;

/**
 * @author garethferrier
 *
 */
public interface NodeRefResolver {

    /**
     * Based on the path to a document find the nodeRef
     * @param path
     * @return nodeRef or null if not found.
     * @throws FileNotFoundException
     */
    NodeRef getNodeRefFromPath(String path) throws AlfrescoRuntimeException, FileNotFoundException;
    
    /**
     * Based on a properly constructed Path parameter return the corresponding NodeRef.
     * 
     * @param path
     * @return nodeRef or null if not found
     * @throws AlfrescoRuntimeException
     * @throws FileNotFoundException
     */
    NodeRef getNodeRefFromPath(Path path) throws AlfrescoRuntimeException, FileNotFoundException;

    /**
     * See above, but with parameter to use the full path
     * 
     * @param path
     * @param useFullPath
     * @return noderef or null if not found
     * @throws AlfrescoRuntimeException
     * @throws FileNotFoundException
     */
    NodeRef getNodeRefFromPath(String path, boolean useFullPath) throws AlfrescoRuntimeException, FileNotFoundException;

    /**
     * Input string is expected to just be the GUID for the nodeRef, therefore no store references included.
     * @param nodeRefStr
     * @return nodeRef or null if not found
     * @throws AlfrescoRuntimeException
     */
    NodeRef getNodeRefFromGUID(String nodeRefStr) throws AlfrescoRuntimeException;
    
    /**
     * Given a string attempt to parse a node ref from it 
     * 
     * @param nodeString
     * @return nodeRef or null if not found.
     * @throws AlfrescoRuntimeException
     */
    NodeRef getNodeRef(String nodeString) throws AlfrescoRuntimeException;
    
    /**
     * Utility method to generate a shortened version of a path from the parameter
     * 
     * @param path to have shortened.
     * @return
     */
    String getShortPathString(Path path);
    
    /**
     * Spring setter.
     * 
     * @param nodeService
     */
    void setNodeService(NodeService nodeService);
    
    /**
     * From the given node ref extract the site that the node is located within.
     * 
     * @param node
     * @return String name of the site
     */
    String getSiteName(NodeRef node);

}
