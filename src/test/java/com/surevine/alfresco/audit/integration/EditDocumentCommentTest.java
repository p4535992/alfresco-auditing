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

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.EditDocumentCommentAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class EditDocumentCommentTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public EditDocumentCommentTest() {
        super(new EditDocumentCommentAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulEdit() {
        try {
            mockRequest
                    .setRequestURI("/alfresco/s/api/comment/node/workspace/SpacesStore/de672fc3-277c-4fda-95d5-23101aa5c372");

            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.SITE, TEST_SITE);
            json.put(AlfrescoJSONKeys.CONTAINER, "documentLibrary");
            json.put(AlfrescoJSONKeys.PAGE, "document-details");

            JSONObject nodeRefJson  = new JSONObject();
            nodeRefJson.put(AlfrescoJSONKeys.NODEREF, TEST_FILENODEREF_STRING);
            json.put("pageParams", nodeRefJson.toString());
            json.put(AlfrescoJSONKeys.ITEM_TITLE, TEST_FILE);
            json.put(AlfrescoJSONKeys.CONTENT, "<p>this is a an edited comment on a document");

            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            assertEquals(TEST_FILE, audited.getSource());

        } catch (Exception e) {
            fail();

        }
    }
}
