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
package com.surevine.alfresco.audit.integration;

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
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
