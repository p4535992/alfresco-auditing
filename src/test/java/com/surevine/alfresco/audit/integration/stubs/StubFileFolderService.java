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

import java.util.List;

import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileFolderServiceType;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.model.SubFolderFilter;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;


public class StubFileFolderService implements FileFolderService {

	public FileInfo copy(NodeRef sourceNodeRef, NodeRef targetParentRef,
			String newName) throws FileExistsException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo create(NodeRef parentNodeRef, String name, QName typeQName)
			throws FileExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo create(NodeRef parentNodeRef, String name, QName typeQName,
			QName assocQName) throws FileExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(NodeRef nodeRef) {
		// TODO Auto-generated method stub

	}

	public boolean exists(NodeRef nodeRef) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Depending on whether or not the nodeRef string begins with 'Folder' construct a FileInfo which will respond
	 * correctly when accessed to see if it is a folder.
	 * 
	 * @see org.alfresco.service.cmr.model.FileFolderService#getFileInfo(org.alfresco.service.cmr.repository.NodeRef)
	 */
	public FileInfo getFileInfo(NodeRef nodeRef) {

	    if(nodeRef.toString().contains("Folder")) {
	         return new StubFileInfo(true);
	    }
	        return new StubFileInfo(false);	    
	}

	public List<FileInfo> getNamePath(NodeRef rootNodeRef, NodeRef nodeRef)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentReader getReader(NodeRef nodeRef) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileFolderServiceType getType(QName typeQName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentWriter getWriter(NodeRef nodeRef) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FileInfo> list(NodeRef contextNodeRef) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<FileInfo> listFiles(NodeRef folderNodeRef) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FileInfo> listFolders(NodeRef contextNodeRef) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo move(NodeRef sourceNodeRef, NodeRef targetParentRef,
			String newName) throws FileExistsException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo rename(NodeRef fileFolderRef, String newName)
			throws FileExistsException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo resolveNamePath(NodeRef rootNodeRef,
			List<String> pathElements) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FileInfo> search(NodeRef contextNodeRef, String namePattern,
			boolean includeSubFolders) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FileInfo> search(NodeRef contextNodeRef, String namePattern,
			boolean fileSearch, boolean folderSearch, boolean includeSubFolders) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef searchSimple(NodeRef contextNodeRef, String name) {
		// TODO Auto-generated method stub
		return null;
	}

    public FileInfo makeFolders(NodeRef arg0, List<String> arg1, QName arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<FileInfo> listDeepFolders(NodeRef arg0, SubFolderFilter arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    public FileInfo move(NodeRef arg0, NodeRef arg1, NodeRef arg2, String arg3) throws FileExistsException,
            FileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileInfo moveFrom(NodeRef arg0, NodeRef arg1, NodeRef arg2, String arg3) throws FileExistsException,
            FileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<FileInfo> removeHiddenFiles(List<FileInfo> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileInfo resolveNamePath(NodeRef arg0, List<String> arg1, boolean arg2) throws FileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

}
