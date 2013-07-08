package com.surevine.alfresco.audit.integration;

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.RemoveDeletionMarkAuditEventListener;

public class RemoveDeletionMarkTest extends AbstractAuditIntegrationTestBase {

    public RemoveDeletionMarkTest()
    {
        super(new RemoveDeletionMarkAuditEventListener());
    }
    
    @Test
    public void testRemoveMarkByPath() throws Exception 
    {

        mockRequest.setParameter("path", "/app:company_home/st:sites/cm:mytestsite/cm:wiki/cm:testing_audit");
        mockRequest.setRequestURI("/alfresco/s/" + RemoveDeletionMarkAuditEventListener.URI_DESIGNATOR);
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals(AbstractAuditIntegrationTestBase.TEST_SITE));
        assertTrue(audited.getAction().equals(RemoveDeletionMarkAuditEventListener.ACTION));
    }
    
    @Test
    public void testRemoveMarkByNodeRef() throws Exception 
    {

        mockRequest.setParameter("nodeRef", "workspace://1/2");
        mockRequest.setRequestURI(RemoveDeletionMarkAuditEventListener.URI_DESIGNATOR);
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals(AbstractAuditIntegrationTestBase.TEST_SITE));
        assertTrue(audited.getAction().equals(RemoveDeletionMarkAuditEventListener.ACTION));
    }
    
    @Test
    public void testRemoveMarkByDoclibNodeRef() throws Exception 
    {

        mockRequest.setParameter("nodeRef", "workspace://1/2");
        mockRequest.setRequestURI("/slingshot/doclib/action/" + RemoveDeletionMarkAuditEventListener.URI_DESIGNATOR);
        mockRequest.setContent("".getBytes());

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
        Auditable audited = getSingleAuditedEvent();
        
        verifyGenericAuditMetadata(audited);
        assertTrue(audited.getSite().equals(AbstractAuditIntegrationTestBase.TEST_SITE));
        assertTrue(audited.getAction().equals(RemoveDeletionMarkAuditEventListener.ACTION));
    }
    
}
