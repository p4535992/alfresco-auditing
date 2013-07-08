package com.surevine.alfresco.audit.integration;

import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.ImmediateArchiveAuditEventListener;

public class ImmediateArchiveTest extends AbstractAuditIntegrationTestBase {

    public ImmediateArchiveTest()
    {
        super(new ImmediateArchiveAuditEventListener());
    }
    
    @Test
    public void testArchiveByPath() throws Exception 
    {
        mockRequest.setParameter("path", AbstractAuditIntegrationTestBase.TEST_FOLDER);
        mockRequest.setRequestURI("/alfresco/s/sv-theme/delete/archive");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals("mytestsite"));
    }
    
    @Test
    public void testUndeleteByNodeRef() throws Exception 
    {

        mockRequest.setParameter(AlfrescoJSONKeys.NODEREF, AbstractAuditIntegrationTestBase.TEST_FILENODEREF_STRING);
        mockRequest.setRequestURI("/sv-theme/delete/archive");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals("mytestsite"));
    }
    
    @Test
    public void testUndeleteByDoclibNodeRef() throws Exception 
    {
        mockRequest.setParameter(AlfrescoJSONKeys.NODEREF, AbstractAuditIntegrationTestBase.TEST_FILENODEREF_STRING);
        mockRequest.setRequestURI("/slingshot/doclib/action/sv-theme/delete/archive");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals("mytestsite"));
    }

}
