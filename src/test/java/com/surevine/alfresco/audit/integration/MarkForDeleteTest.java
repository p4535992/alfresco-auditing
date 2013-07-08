package com.surevine.alfresco.audit.integration;

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.integration.AbstractAuditIntegrationTestBase;
import com.surevine.alfresco.audit.listeners.MarkForDeleteAuditEventListener;

public class MarkForDeleteTest extends AbstractAuditIntegrationTestBase {

    
    public MarkForDeleteTest()
    {
        super(new MarkForDeleteAuditEventListener());
    }
    
    @Test
    public void testDeleteByPath() throws Exception 
    {

        mockRequest.setParameter("path", "/app:company_home/st:sites/cm:mytestsite/cm:wiki/cm:testing_audit");
        mockRequest.setRequestURI("/alfresco/s/sv-theme/delete/markForDelete");
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals(AbstractAuditIntegrationTestBase.TEST_SITE));
        assertTrue(audited.getAction().equals(MarkForDeleteAuditEventListener.ACTION));
    }
    
    @Test
    public void testDeleteByNodeRef() throws Exception 
    {

        mockRequest.setParameter("nodeRef", "workspace://1/2");
        mockRequest.setRequestURI(MarkForDeleteAuditEventListener.URI_DESIGNATOR);
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals(AbstractAuditIntegrationTestBase.TEST_SITE));
        assertTrue(audited.getAction().equals(MarkForDeleteAuditEventListener.ACTION));
    }
    
    @Test
    public void testDeleteByDoclibNodeRef() throws Exception 
    {

        mockRequest.setParameter("nodeRef", "workspace://1/2");
        mockRequest.setRequestURI("/slingshot/doclib/action/" + MarkForDeleteAuditEventListener.URI_DESIGNATOR);
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals(AbstractAuditIntegrationTestBase.TEST_SITE));
        assertTrue(audited.getAction().equals(MarkForDeleteAuditEventListener.ACTION));
    }
    
}
