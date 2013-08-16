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

import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.LockDocumentAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class LockDocumentTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public LockDocumentTest() {
        super(new LockDocumentAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessFromListView() {

        try {

            // Create an empty JSON string for the post data.
            JSONObject json = new JSONObject();

            mockRequest
                    .setRequestURI("/alfresco/s/slingshot/doclib/action/checkout/node/workspace/SpacesStore/11111111-1111-1111-1111-111111111111");
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_SITE, audited.getSite());
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSuccessFromDetailsView() {
        try {
            // Create an empty JSON string for the post data.
            JSONObject json = new JSONObject();

            mockRequest
                    .setRequestURI("/alfresco/s/slingshot/doclib/action/checkout/site/mytestsite/documentLibrary/" + TEST_FILE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(TEST_SITE, audited.getSite());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
