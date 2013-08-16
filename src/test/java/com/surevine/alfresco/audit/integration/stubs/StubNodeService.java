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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.dictionary.InvalidAspectException;
import org.alfresco.service.cmr.dictionary.InvalidTypeException;
import org.alfresco.service.cmr.repository.AssociationExistsException;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.InvalidChildAssociationRefException;
import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.InvalidStoreRefException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.repository.StoreExistsException;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.repository.NodeRef.Status;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.QNamePattern;

import static com.surevine.alfresco.audit.integration.AbstractAuditIntegrationTestBase.*;

import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * @author garethferrier
 * 
 */
public class StubNodeService implements NodeService {

    public StubNodeService() {

        ArrayList<String> openGroups = new ArrayList<String>();
        openGroups.add(TEST_OPEN_GROUP1);
        openGroups.add(TEST_OPEN_GROUP2);

        ArrayList<String> closedGroups = new ArrayList<String>();
        closedGroups.add(TEST_CLOSED_GROUP1);
        closedGroups.add(TEST_CLOSED_GROUP2);

        ArrayList<String> organisations = new ArrayList<String>();
        organisations.add(TEST_ORGANISATION1);
        organisations.add(TEST_ORGANISATION2);

        properties.put(EnhancedSecurityLabel.NATIONALITY_OWNER, TEST_NATIONALITY_OWNER);
        properties.put(EnhancedSecurityLabel.PROTECTIVE_MARKING, TEST_PROTECTIVE_MARKING);
        properties.put(EnhancedSecurityLabel.OPEN_GROUPS, openGroups);
        properties.put(EnhancedSecurityLabel.CLOSED_GROUPS, closedGroups);
        properties.put(EnhancedSecurityLabel.ORGANISATIONS, organisations);
        properties.put(EnhancedSecurityLabel.FREE_FORM_CAVEATS, TEST_FREEFORM_CAVEAT);
        properties.put(EnhancedSecurityLabel.NATIONALITY_CAVEATS, TEST_NATIONALITY_CAVEATS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#addAspect(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName, java.util.Map)
     */
    public void addAspect(NodeRef nodeRef, QName aspectTypeQName, Map<QName, Serializable> aspectProperties)
            throws InvalidNodeRefException, InvalidAspectException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#addChild(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.namespace.QName,
     * org.alfresco.service.namespace.QName)
     */
    public ChildAssociationRef addChild(NodeRef parentRef, NodeRef childRef, QName assocTypeQName, QName qname)
            throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#addChild(java.util.Collection,
     * org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.namespace.QName,
     * org.alfresco.service.namespace.QName)
     */
    public List<ChildAssociationRef> addChild(Collection<NodeRef> parentRefs, NodeRef childRef, QName assocTypeQName,
            QName qname) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#addProperties(org.alfresco.service.cmr.repository.NodeRef,
     * java.util.Map)
     */
    public void addProperties(NodeRef nodeRef, Map<QName, Serializable> properties) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#createAssociation(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.namespace.QName)
     */
    public AssociationRef createAssociation(NodeRef sourceRef, NodeRef targetRef, QName assocTypeQName)
            throws InvalidNodeRefException, AssociationExistsException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#createNode(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName, org.alfresco.service.namespace.QName, org.alfresco.service.namespace.QName)
     */
    public ChildAssociationRef createNode(NodeRef parentRef, QName assocTypeQName, QName assocQName, QName nodeTypeQName)
            throws InvalidNodeRefException, InvalidTypeException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#createNode(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName, org.alfresco.service.namespace.QName, org.alfresco.service.namespace.QName,
     * java.util.Map)
     */
    public ChildAssociationRef createNode(NodeRef parentRef, QName assocTypeQName, QName assocQName,
            QName nodeTypeQName, Map<QName, Serializable> properties) throws InvalidNodeRefException,
            InvalidTypeException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#createStore(java.lang.String, java.lang.String)
     */
    public StoreRef createStore(String protocol, String identifier) throws StoreExistsException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#deleteNode(org.alfresco.service.cmr.repository.NodeRef)
     */
    public void deleteNode(NodeRef nodeRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#deleteStore(org.alfresco.service.cmr.repository.StoreRef)
     */
    public void deleteStore(StoreRef storeRef) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#exists(org.alfresco.service.cmr.repository.StoreRef)
     */
    public boolean exists(StoreRef storeRef) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * The NodeRef.isNodeRef() appears quite relaxed so use this to decide if a node ref is genuine. Return true if the
     * toString() begins with 'workspace'
     */
    public boolean exists(NodeRef nodeRef) {
        return nodeRef.toString().startsWith("workspace") || nodeRef.toString().startsWith("versionStore");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getAspects(org.alfresco.service.cmr.repository.NodeRef)
     */
    public Set<QName> getAspects(NodeRef nodeRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getAssoc(java.lang.Long)
     */
    public AssociationRef getAssoc(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getChildAssocs(org.alfresco.service.cmr.repository.NodeRef)
     */
    public List<ChildAssociationRef> getChildAssocs(NodeRef nodeRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getChildAssocs(org.alfresco.service.cmr.repository.NodeRef,
     * java.util.Set)
     */
    public List<ChildAssociationRef> getChildAssocs(NodeRef nodeRef, Set<QName> childNodeTypeQNames) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getChildAssocs(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QNamePattern, org.alfresco.service.namespace.QNamePattern)
     */
    public List<ChildAssociationRef> getChildAssocs(NodeRef nodeRef, QNamePattern typeQNamePattern,
            QNamePattern qnamePattern) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getChildAssocs(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QNamePattern, org.alfresco.service.namespace.QNamePattern, boolean)
     */
    public List<ChildAssociationRef> getChildAssocs(NodeRef nodeRef, QNamePattern typeQNamePattern,
            QNamePattern qnamePattern, boolean preload) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#getChildAssocsByPropertyValue(org.alfresco.service.cmr.repository
     * .NodeRef, org.alfresco.service.namespace.QName, java.io.Serializable)
     */
    public List<ChildAssociationRef> getChildAssocsByPropertyValue(NodeRef nodeRef, QName propertyQName,
            Serializable value) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#getChildAssocsWithoutParentAssocsOfType(org.alfresco.service.
     * cmr.repository.NodeRef, org.alfresco.service.namespace.QName)
     */
    public Collection<ChildAssociationRef> getChildAssocsWithoutParentAssocsOfType(NodeRef parent, QName assocTypeQName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getChildByName(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName, java.lang.String)
     */
    public NodeRef getChildByName(NodeRef nodeRef, QName assocTypeQName, String childName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#getChildrenByName(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName, java.util.Collection)
     */
    public List<ChildAssociationRef> getChildrenByName(NodeRef nodeRef, QName assocTypeQName,
            Collection<String> childNames) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getNodeAclId(org.alfresco.service.cmr.repository.NodeRef)
     */
    public Long getNodeAclId(NodeRef nodeRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getNodeStatus(org.alfresco.service.cmr.repository.NodeRef)
     */
    public Status getNodeStatus(NodeRef nodeRef) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getParentAssocs(org.alfresco.service.cmr.repository.NodeRef)
     */
    public List<ChildAssociationRef> getParentAssocs(NodeRef nodeRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getParentAssocs(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QNamePattern, org.alfresco.service.namespace.QNamePattern)
     */
    public List<ChildAssociationRef> getParentAssocs(NodeRef nodeRef, QNamePattern typeQNamePattern,
            QNamePattern qnamePattern) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Simply recreate a path, regardless of the noderef that was entered.
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getPath(org.alfresco.service.cmr.repository.NodeRef)
     */
    public Path getPath(NodeRef nodeRef) throws InvalidNodeRefException {
        Path path = new Path();
        path.append(new Path.AttributeElement(QName.createQName("http://www.alfresco.org/model/application/1.0",
                "company_home")));
        path.append(new Path.AttributeElement(QName.createQName("http://www.alfresco.org/model/site/1.0", "sites")));
        path.append(new Path.AttributeElement(QName.createQName("http://www.alfresco.org/model/content/1.0",
                "mytestsite")));
        path.append(new Path.AttributeElement(QName.createQName("http://www.alfresco.org/model/content/1.0",
                "documentLibrary")));
        path.append(new Path.AttributeElement(QName.createQName("http://www.alfresco.org/model/content/1.0",
                "mytestsite")));
        path.append(new Path.AttributeElement(QName.createQName("http://www.alfresco.org/model/content/1.0",
                "folderToCopyFilesTo")));
        return path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getPaths(org.alfresco.service.cmr.repository.NodeRef,
     * boolean)
     */
    public List<Path> getPaths(NodeRef nodeRef, boolean primaryOnly) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#getPrimaryParent(org.alfresco.service.cmr.repository.NodeRef)
     */
    public ChildAssociationRef getPrimaryParent(NodeRef nodeRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    private Map<QName, Serializable> properties = new HashMap<QName, Serializable>();

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getProperties(org.alfresco.service.cmr.repository.NodeRef)
     */
    public Map<QName, Serializable> getProperties(NodeRef nodeRef) throws InvalidNodeRefException {

        // Only return the ESL properties if its of the type which has an ESL
        if (!TEST_DOCUMENT_COMMENT_NODEREF_STRING.equals(nodeRef.toString())) {
            return properties;
        } else {
            return new HashMap<QName, Serializable>();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getProperty(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName)
     */
    public Serializable getProperty(NodeRef nodeRef, QName qname) throws InvalidNodeRefException {

        if (properties.containsKey(qname)) {
            return properties.get(qname);
        }

        if (ContentModel.PROP_NAME.equals(qname)) {
            if (TEST_FILENODEREF_STRING.equals(nodeRef.toString())) {
                return TEST_FILE;
            } else if (TEST_FOLDERNODEREF_STRING.equals(nodeRef.toString())) {
                return TEST_FOLDER;
            } else if (TEST_WIKIPAGE_NODEREF_STRING.equals(nodeRef.toString())) {
                return TEST_WIKIPAGE_NAME;
            } else if (TEST_DISCUSSION_TOPIC_NODEREF_STRING.equals(nodeRef.toString())) {
                return TEST_DISCUSSION_TOPIC_NAME;
            } else if (TEST_DISCUSSION_REPLY_NODEREF_STRING.equals(nodeRef.toString())) {
                return TEST_DISCUSSION_REPLY_NAME;
            } else {
                return null;
            }
        } else if (ContentModel.PROP_HOMEFOLDER.equals(qname)) {
            return TEST_FOLDER;
        } else if (ContentModel.PROP_VERSION_LABEL.equals(qname)) {
            if (TEST_VERSIONED_WIKIPAGE_NODEREF_STRING.equals(nodeRef.toString())) {
                return TEST_VERSIONED_WIKIPAGE_VERSION;
            } else if (TEST_DISCUSSION_TOPIC_NODEREF_STRING.equals(nodeRef.toString())) {
                return AbstractAuditEventListener.NO_VERSION_STRING;
            } else {
                return TEST_VERSION_LABEL;
            }

        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getRootNode(org.alfresco.service.cmr.repository.StoreRef)
     */
    public NodeRef getRootNode(StoreRef storeRef) throws InvalidStoreRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getSourceAssocs(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QNamePattern)
     */
    public List<AssociationRef> getSourceAssocs(NodeRef targetRef, QNamePattern qnamePattern)
            throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#getStoreArchiveNode(org.alfresco.service.cmr.repository.StoreRef)
     */
    public NodeRef getStoreArchiveNode(StoreRef storeRef) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getStores()
     */
    public List<StoreRef> getStores() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getTargetAssocs(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QNamePattern)
     */
    public List<AssociationRef> getTargetAssocs(NodeRef sourceRef, QNamePattern qnamePattern)
            throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#getType(org.alfresco.service.cmr.repository.NodeRef)
     */
    public QName getType(NodeRef nodeRef) throws InvalidNodeRefException {

        if (nodeRef.toString().equals(TEST_FILENODEREF_STRING)) {
            return ContentModel.TYPE_CONTENT;
        } else if (nodeRef.toString().equals(TEST_FOLDERNODEREF_STRING)) {
            return ContentModel.TYPE_FOLDER;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#hasAspect(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName)
     */
    public boolean hasAspect(NodeRef nodeRef, QName aspectTypeQName) throws InvalidNodeRefException,
            InvalidAspectException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#moveNode(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.namespace.QName,
     * org.alfresco.service.namespace.QName)
     */
    public ChildAssociationRef moveNode(NodeRef nodeToMoveRef, NodeRef newParentRef, QName assocTypeQName,
            QName assocQName) throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#removeAspect(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName)
     */
    public void removeAspect(NodeRef nodeRef, QName aspectTypeQName) throws InvalidNodeRefException,
            InvalidAspectException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#removeAssociation(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.namespace.QName)
     */
    public void removeAssociation(NodeRef sourceRef, NodeRef targetRef, QName assocTypeQName)
            throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#removeChild(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.cmr.repository.NodeRef)
     */
    public void removeChild(NodeRef parentRef, NodeRef childRef) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.alfresco.service.cmr.repository.NodeService#removeChildAssociation(org.alfresco.service.cmr.repository.
     * ChildAssociationRef)
     */
    public boolean removeChildAssociation(ChildAssociationRef childAssocRef) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#removeProperty(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName)
     */
    public void removeProperty(NodeRef nodeRef, QName qname) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#removeSeconaryChildAssociation(org.alfresco.service.cmr.repository
     * .ChildAssociationRef)
     */
    public boolean removeSeconaryChildAssociation(ChildAssociationRef childAssocRef) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#restoreNode(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.namespace.QName,
     * org.alfresco.service.namespace.QName)
     */
    public NodeRef restoreNode(NodeRef archivedNodeRef, NodeRef destinationParentNodeRef, QName assocTypeQName,
            QName assocQName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.alfresco.service.cmr.repository.NodeService#setChildAssociationIndex(org.alfresco.service.cmr.repository.
     * ChildAssociationRef, int)
     */
    public void setChildAssociationIndex(ChildAssociationRef childAssocRef, int index)
            throws InvalidChildAssociationRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#setProperties(org.alfresco.service.cmr.repository.NodeRef,
     * java.util.Map)
     */
    public void setProperties(NodeRef nodeRef, Map<QName, Serializable> properties) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#setProperty(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName, java.io.Serializable)
     */
    public void setProperty(NodeRef nodeRef, QName qname, Serializable value) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.alfresco.service.cmr.repository.NodeService#setType(org.alfresco.service.cmr.repository.NodeRef,
     * org.alfresco.service.namespace.QName)
     */
    public void setType(NodeRef nodeRef, QName typeQName) throws InvalidNodeRefException {
        // TODO Auto-generated method stub

    }

    public List<NodeRef> findNodes(FindNodeParameters arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<NodeRef> getAllRootNodes(StoreRef arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ChildAssociationRef> getChildAssocs(NodeRef arg0, QName arg1, QName arg2, int arg3, boolean arg4)
            throws InvalidNodeRefException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeRef getNodeRef(Long arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
