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

import java.util.Set;

import org.alfresco.repo.security.authentication.AuthenticationException;
import org.alfresco.service.cmr.security.AuthenticationService;

public class StubAuthenticationService implements AuthenticationService {

    public void authenticate(String arg0, char[] arg1) throws AuthenticationException {
        // TODO Auto-generated method stub

    }

    public void authenticateAsGuest() throws AuthenticationException {
        // TODO Auto-generated method stub

    }

    public boolean authenticationExists(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void clearCurrentSecurityContext() {
        // TODO Auto-generated method stub

    }

    public boolean getAuthenticationEnabled(String arg0) throws AuthenticationException {
        // TODO Auto-generated method stub
        return false;
    }

    public String getCurrentTicket() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCurrentTicket(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCurrentUserName() throws AuthenticationException {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<String> getDefaultAdministratorUserNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<String> getDefaultGuestUserNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<String> getDomains() {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<String> getDomainsThatAllowUserCreation() {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<String> getDomainsThatAllowUserDeletion() {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<String> getDomiansThatAllowUserPasswordChanges() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNewTicket(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean guestUserAuthenticationAllowed() {
        // TODO Auto-generated method stub
        return false;
    }

    public void invalidateTicket(String arg0, String arg1) throws AuthenticationException {
        // TODO Auto-generated method stub

    }

    public void invalidateUserSession(String arg0) throws AuthenticationException {
        // TODO Auto-generated method stub

    }

    public boolean isCurrentUserTheSystemUser() {
        // TODO Auto-generated method stub
        return false;
    }

    public void validate(String arg0, String arg1) throws AuthenticationException {
        // TODO Auto-generated method stub

    }

    public String getNewTicket() {
        // TODO Auto-generated method stub
        return null;
    }

    public void invalidateTicket(String arg0) throws AuthenticationException {
        // TODO Auto-generated method stub
        
    }

    public void validate(String arg0) throws AuthenticationException {
        // TODO Auto-generated method stub
        
    }

}
