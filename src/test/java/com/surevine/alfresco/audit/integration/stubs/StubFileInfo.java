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
import java.util.Date;
import java.util.Map;

import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;

public class StubFileInfo implements FileInfo {

    public boolean isFolder = false;
    
    public StubFileInfo(boolean folder) {
        this.isFolder = folder;
    }
    
	public ContentData getContentData() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getCreatedDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef getLinkNodeRef() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getModifiedDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return "testFile.txt";
	}

	public NodeRef getNodeRef() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<QName, Serializable> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is used to test whether or not a node ref is a file or a folder.
	 * 
	 * @see org.alfresco.service.cmr.model.FileInfo#isFolder()
	 */
	public boolean isFolder() {
		return isFolder;
	}

	public boolean isLink() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean isHidden() {
        // TODO Auto-generated method stub
        return false;
    }

}
