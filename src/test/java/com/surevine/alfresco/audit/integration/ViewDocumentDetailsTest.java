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
import com.surevine.alfresco.audit.listeners.ViewDocumentDetailsAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class ViewDocumentDetailsTest extends AbstractAuditIntegrationTestBase {
    /**
     * Constructor.
     */
    public ViewDocumentDetailsTest() {
        super(new ViewDocumentDetailsAuditEventListener());
    }

    /**
     * Test sunny day scenario. The URL destination changed significantly from 3.2 to 3.4, in 3.2 the document details
     * and the nodeRef were encoded in the URI. For 3.4 the URI is generic and a parameter contains the noderef.
     */
    @Test
    public void testSuccess() {

        try {
            mockRequest.setRequestURI("/alfresco/wcs/api/metadata");
            mockRequest.setParameter("nodeRef", TEST_FILENODEREF_STRING);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);

            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());

            // just assert that the details are non-null.
            assertNotNull(audited.getDetails());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * This test is to make sure that a false-positive recorded at site is not reproduced. The uri that was being
     * falsely reported was - ../doclib/doclist/documents/node/alfresco/sites/home
     */
    @Test
    public void testLandingPageNotRecorded() {
        try {
            mockRequest.setRequestURI("/alfresco/s/slingshot/doclib/doclist/documents/node/alfresco/sites/home");

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            // Make sure that no event has been generated.
            getSingleAuditedEvent();

            // If we get here something has gone awry
            fail();

        } catch (Exception e) {
            // We hope to drop out here.
            assertTrue(true);
        }
    }

    /**
     * The behaviour when a user requests a document with a nodeRef that is valid but doesn't exist needs to be
     * dealt with, also this is the same behaviour for a document which the user is not allowed to access.  The
     * response status is 200, but the content of the response is CRLF.
     */
    public void testUnavailableDocumentReportedAsFailure() {
        try {

            mockRequest.setRequestURI("/alfresco/wcs/api/metadata");
            mockRequest.setParameter("nodeRef", TEST_FILENODEREF_STRING);
            
            ResponseModifiableMockFilterChain modifiableFilterChain = new ResponseModifiableMockFilterChain(
                    "\r\n", HttpServletResponse.SC_OK);
            
            springAuditFilterBean.doFilter(mockRequest, mockResponse, modifiableFilterChain);
            
            Auditable audited = getSingleAuditedEvent();
            assertFalse(audited.isSuccess());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    /**
     * To construct the document-details page 'share' makes two calls to the same 'metadata' api, this means that
     * duplicate VIEW events are being recorded for a single user access.  The only difference in the calls is that
     * on one of them a second parameter, beyond the 'nodeRef', is present - 'shortQNames' - this will be used as
     * an additional discriminator alongside the URL.
     */
    public void testNoDuplicateEventRecorded() {
        try {
            mockRequest.setRequestURI("/alfresco/wcs/api/metadata");
            mockRequest.setParameter("nodeRef", TEST_FILENODEREF_STRING);
            mockRequest.setParameter("shortQNames", Boolean.TRUE.toString());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            getSingleAuditedEvent();
            
            fail();

        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
