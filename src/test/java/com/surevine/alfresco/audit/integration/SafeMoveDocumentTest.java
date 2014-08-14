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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.SafeMoveDocumentAuditEventListener;

/**
 * @author jonnyh
 * 
 */

public class SafeMoveDocumentTest extends AbstractAuditIntegrationTestBase {

    public static final String DESTINATION_FOLDER = "MOVE_FOLDER";
    public static final String REQUEST_URI_FIXTURE = "/alfresco/s/slingshot/doclib/action/safe-move-to/node/workspace/workspace/"
            + TEST_FOLDERNODEREF_ID;

    /**
     * Constructor.
     */
    public SafeMoveDocumentTest() {
        super(new SafeMoveDocumentAuditEventListener());
    }

    /**
     * Test sunny day scenario. This test should also assert the contents of returned item.
     */
    @Test
    public void testSuccess() {

        try {

            // Create an empty JSON string for the post data.
            JSONObject json = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());
            
            // Initialise the response.
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("totalResults", 2);
            responseJSON.put("overallSuccess", true);

            ResponseModifiableMockFilterChain modifiableFilterChain = new ResponseModifiableMockFilterChain(
                    responseJSON.toString(), HttpServletResponse.SC_OK);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, modifiableFilterChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());

            assertEquals(TEST_SITE, audited.getSite());
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            assertEquals(TEST_FOLDER_PATH, audited.getDetails());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Test that multiple documents can be moved and that each one creates an auditable event.
     */
    @Test
    public void testMoveMultipleDocuments() {
        try {
            JSONObject json = new JSONObject();

            eslFixture.addToJSON(json);

            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);

            arr.put("workspace://SpacesStore/12345678-1234-1234-1234-123456789012");
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            // Now check that two events have been created.
            assertEquals(2, countRowsInTable(AUDIT_TABLE_NAME));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMultiMoveOfFoldersAndDocuments() {
        try {
            JSONObject json = new JSONObject();

            eslFixture.addToJSON(json);

            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);

            arr.put(TEST_FOLDERNODEREF_STRING);
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            // Now check that two events have been created.
            assertEquals(2, countRowsInTable(AUDIT_TABLE_NAME));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMoveOfFolder() {
        try {
            JSONObject json = new JSONObject();

            eslFixture.addToJSON(json);

            JSONArray arr = new JSONArray();
            arr.put(TEST_FOLDERNODEREF_STRING);
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            // Check that the security label is different for folder, as they don't get one.
            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Created in response to JIRA INT-261.
     * 
     * When a multi-move is attempted that any element of it fails then the entire operation fails. A good example (from
     * system tests) is where a multi-move is attempted where one of the documents has another document in the target
     * folder with the same name. Both should be reported with success failing. An example of what the JSON looks like
     * is given below - { "totalResults": 2, "overallSuccess": false, "successCount": 1, "failureCount": 1, "results": [
     * { "action": "moveFile", "id": "audit_test_1.txt", "nodeRef":
     * "workspace:\/\/SpacesStore\/d4e795a9-caee-4a0f-bbe1-9ba7e26416c6", "type": "document", "success": true }, {
     * "action": "moveFile", "id": "1", "nodeRef": "workspace:\/\/SpacesStore\/41e72ed6-d126-4a38-ae4e-cd38f6b0ceb8",
     * "type": "document", "success": false } ] }
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPartialFailureOfAMove() {

        // Need to be able to seed the response object with the failure JSON
        JSONObject failureToMoveSuccessfully = new JSONObject();

        try {
            // Initialise the request object
            JSONObject json = new JSONObject();

            eslFixture.addToJSON(json);

            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);

            arr.put("workspace://SpacesStore/12345678-1234-1234-1234-123456789012");
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            // Initialise the response.
            failureToMoveSuccessfully.put("totalResults", 2);
            failureToMoveSuccessfully.put("overallSuccess", false);

            ResponseModifiableMockFilterChain modifiableFilterChain = new ResponseModifiableMockFilterChain(
                    failureToMoveSuccessfully.toString(), HttpServletResponse.SC_OK);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, modifiableFilterChain);

            assertEquals(2, countRowsInTable(AUDIT_TABLE_NAME));

            List<Auditable> events = jdbcTemplate.query(getSimpleQuery(), new AuditRowMapper());

            for (Auditable event : events) {
                assertFalse(event.isSuccess());
                assertEquals("200:false", event.getDetails());
            }

        } catch (Exception e) {
            fail();
        }
    }
}
