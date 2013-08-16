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

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.CreateDocumentCommentAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class CreateDocumentCommentTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public CreateDocumentCommentTest() {
        super(new CreateDocumentCommentAuditEventListener());
    }

    /**
     * Test the sunny day scenario.
     */
    @Test
    public void testSuccessfulCreation() {
        try {

            mockRequest
                    .setRequestURI("/alfresco/s/api/node/workspace/SpacesStore/11111111-1111-1111-1111-111111111111/comments");

            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.NODEREF, TEST_FILENODEREF_STRING);
            json.put(AlfrescoJSONKeys.SITE, TEST_SITE);
            json.put(AlfrescoJSONKeys.CONTAINER, "documentLibrary");
            json.put(AlfrescoJSONKeys.ITEM_TITLE, TEST_FILE);
            json.put(AlfrescoJSONKeys.CONTENT, "<p>this is a comment on the document");

            // The document comment appears to use empty values in the JSON.
            emptyESL.addToJSON(json);

            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());
            
            // These shouldn't be the same as it is the noderef of the parent that is provided in the URL
            assertNotSame(TEST_FILENODEREF_STRING, audited.getNodeRef());
            assertEquals(TEST_FILENODEREF_STRING, audited.getSource());
            assertEquals(AbstractAuditEventListener.NO_VERSION_STRING, audited.getVersion());

        } catch (Exception e) {
            fail();
        }
    }

}
