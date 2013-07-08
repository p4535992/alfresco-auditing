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
package com.surevine.alfresco.audit.integration;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.DownloadDocumentAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class DownloadDocumentTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public DownloadDocumentTest() {
        super(new DownloadDocumentAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccess() {

        mockRequest.setRequestURI("/alfresco/s/api/node/content/workspace/SpacesStore/" + TEST_FILENODEREF_ID + "/"
                + TEST_FILE);

        try {

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * On the details screen it is possible to download a previous version of a document. The cut needs to be able to
     * cater for that, it is slightly different as the node service is not used, instead the version service is used.
     */
    @Test
    public void testDownloadOfPreviousVersion() {

        mockRequest
                .setRequestURI("/alfresco/s/api/node/content/versionStore/version2Store/" + TEST_FILENODEREF_ID + "/"
                        + TEST_FILE);

        try {

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());
//            assertEquals(TEST_VERSIONED_FILENODEREF_STRING, audited.getNodeRef());
            assertEquals(TEST_SITE, audited.getSite());
            verifyGenericAuditMetadata(audited);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
    
    public void testRequestForUnmodifiedImage() {
        ResponseModifiableMockFilterChain modifiableFilterChain = new ResponseModifiableMockFilterChain(
                "", HttpServletResponse.SC_NOT_MODIFIED);
        
        mockRequest.setRequestURI("/alfresco/wcs/api/node/content/workspace/SpacesStore/" + TEST_FILENODEREF_ID);
        
        try {
            springAuditFilterBean.doFilter(mockRequest, mockResponse, modifiableFilterChain);

            Auditable audited = getSingleAuditedEvent();
            
            assertTrue(audited.isSuccess());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
