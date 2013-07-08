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

import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UnlockDocumentAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class UnlockDocumentTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public UnlockDocumentTest() {
        super(new UnlockDocumentAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccess() {

        try {

            // Create an empty JSON string for the post data.
            JSONObject json = new JSONObject();

            mockRequest
                    .setRequestURI("/alfresco/s/slingshot/doclib/action/cancel-checkout/node/workspace/SpacesStore/11111111-1111-1111-1111-111111111111");
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());
            
            assertEquals(TEST_SITE, audited.getSite());
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
