package com.surevine.alfresco.audit.integration;

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UndeleteAuditEventListener;

public class UndeleteTest extends AbstractAuditIntegrationTestBase {

    public UndeleteTest()
    {
        super(new UndeleteAuditEventListener());
    }
    
    @Test
    public void testUndeleteByPath() throws Exception 
    {

        mockRequest.setParameter("path", AbstractAuditIntegrationTestBase.TEST_FOLDER);
        mockRequest.setRequestURI("/alfresco/s/sv-theme/delete/undelete");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals("mytestsite"));
    }
    
    @Test
    public void testUndeleteByNodeRef() throws Exception 
    {

        mockRequest.setParameter("nodeRef", AbstractAuditIntegrationTestBase.TEST_FILENODEREF_STRING);
        mockRequest.setRequestURI("/sv-theme/delete/undelete");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals("mytestsite"));
    }
    
    @Test
    public void testUndeleteByDoclibNodeRef() throws Exception 
    {
        mockRequest.setParameter("nodeRef", AbstractAuditIntegrationTestBase.TEST_FILENODEREF_STRING);
        mockRequest.setRequestURI("/slingshot/doclib/action/sv-theme/delete/undelete");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals("mytestsite"));
    }
    
}