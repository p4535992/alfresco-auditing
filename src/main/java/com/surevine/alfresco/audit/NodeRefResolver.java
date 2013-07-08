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
