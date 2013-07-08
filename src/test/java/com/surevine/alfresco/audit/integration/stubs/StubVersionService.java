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
package com.surevine.alfresco.audit.integration.stubs;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.version.VersionBaseModel;
import org.alfresco.repo.version.VersionServicePolicies.CalculateVersionLabelPolicy;
import org.alfresco.repo.version.common.VersionImpl;
import org.alfresco.service.cmr.repository.AspectMissingException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.version.ReservedVersionNameException;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionHistory;
import org.alfresco.service.cmr.version.VersionService;
import org.alfresco.service.namespace.QName;

public class StubVersionService implements VersionService {

	public Version createVersion(NodeRef nodeRef,
			Map<String, Serializable> versionProperties)
			throws ReservedVersionNameException, AspectMissingException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Version> createVersion(Collection<NodeRef> nodeRefs,
			Map<String, Serializable> versionProperties)
			throws ReservedVersionNameException, AspectMissingException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Version> createVersion(NodeRef nodeRef,
			Map<String, Serializable> versionProperties, boolean versionChildren)
			throws ReservedVersionNameException, AspectMissingException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteVersion(NodeRef nodeRef, Version version) {
		// TODO Auto-generated method stub

	}

	public void deleteVersionHistory(NodeRef nodeRef)
			throws AspectMissingException {
		// TODO Auto-generated method stub

	}

	public Version getCurrentVersion(NodeRef nodeRef) {
		Map<String, Serializable> versionProps = new HashMap<String, Serializable>();
		versionProps.put(VersionBaseModel.PROP_VERSION_LABEL, "1.0");
		
		return new VersionImpl(versionProps, nodeRef);
	}

	public VersionHistory getVersionHistory(NodeRef nodeRef)
			throws AspectMissingException {
		// TODO Auto-generated method stub
		return null;
	}

	public StoreRef getVersionStoreReference() {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerVersionLabelPolicy(QName typeQName,
			CalculateVersionLabelPolicy policy) {
		// TODO Auto-generated method stub

	}

	public NodeRef restore(NodeRef nodeRef, NodeRef parentNodeRef,
			QName assocTypeQName, QName assocQName) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef restore(NodeRef nodeRef, NodeRef parentNodeRef,
			QName assocTypeQName, QName assocQName, boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

	public void revert(NodeRef nodeRef) {
		// TODO Auto-generated method stub

	}

	public void revert(NodeRef nodeRef, boolean deep) {
		// TODO Auto-generated method stub

	}

	public void revert(NodeRef nodeRef, Version version) {
		// TODO Auto-generated method stub

	}

	public void revert(NodeRef nodeRef, Version version, boolean deep) {
		// TODO Auto-generated method stub

	}

    @Override
    public void ensureVersioningEnabled(NodeRef arg0, Map<QName, Serializable> arg1) {
        // TODO Auto-generated method stub
        
    }

}
