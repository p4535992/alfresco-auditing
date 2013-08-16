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
