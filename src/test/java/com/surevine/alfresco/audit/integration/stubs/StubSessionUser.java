package com.surevine.alfresco.audit.integration.stubs;

import org.alfresco.repo.SessionUser;

import com.surevine.alfresco.audit.integration.AbstractAuditIntegrationTestBase;

public class StubSessionUser implements SessionUser {

    public String getTicket() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUserName() {

        return AbstractAuditIntegrationTestBase.TEST_USER;
    }

}
