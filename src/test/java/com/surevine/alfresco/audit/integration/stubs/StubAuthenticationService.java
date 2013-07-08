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
