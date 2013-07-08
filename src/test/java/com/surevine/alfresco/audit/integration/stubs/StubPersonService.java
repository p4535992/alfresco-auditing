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
import java.util.Map;
import java.util.Set;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;

public class StubPersonService implements PersonService {

	public boolean createMissingPeople() {
		// TODO Auto-generated method stub
		return false;
	}

	public NodeRef createPerson(Map<QName, Serializable> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef createPerson(Map<QName, Serializable> arg0, Set<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deletePerson(String arg0) {
		// TODO Auto-generated method stub

	}

	public Set<NodeRef> getAllPeople() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<QName> getMutableProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef getPeopleContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<NodeRef> getPeopleFilteredByProperty(QName arg0,
			Serializable arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef getPerson(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeRef getPerson(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserIdentifier(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getUserNamesAreCaseSensitive() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean personExists(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCreateMissingPeople(boolean arg0) {
		// TODO Auto-generated method stub

	}

	public void setPersonProperties(String arg0, Map<QName, Serializable> arg1) {
		// TODO Auto-generated method stub

	}

	public void setPersonProperties(String arg0, Map<QName, Serializable> arg1,
			boolean arg2) {
		// TODO Auto-generated method stub

	}

    public void deletePerson(NodeRef arg0) {
        // TODO Auto-generated method stub
        
    }

}
