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
import com.surevine.alfresco.audit.listeners.CopyDocumentAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class CopyDocumentTest extends AbstractAuditIntegrationTestBase {

    public static final String REQUEST_URI_FIXTURE = "/alfresco/s/slingshot/doclib/action/copy-to/site/mytestsite/documentLibrary" + NESTED_TEST_FOLDER;

    /**
     * Default constructor.
     */
    public CopyDocumentTest() {
        super(new CopyDocumentAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulCopy() {

        try {
            JSONObject json = new JSONObject();

            eslFixture.addToJSON(json);

            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(true, audited.isSuccess());
            assertEquals(eslFixture.toString(), audited.getSecLabel().toString());
            assertEquals(NESTED_TEST_FOLDER, audited.getDetails());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test that there is not an error when a document is being uploaded. the problem was that the
     * PostAuditEventListener was trying to create a JSONString from a MIME document.
     */
    @Test
    public void testDocumentUpload() {

        String docContent = "------------GI3GI3KM7gL6ei4GI3gL6cH2cH2GI3"
                + "Content-Disposition: form-data; name='Filename'";

        try {

            mockRequest.setRequestURI("/alfresco/s/api/upload");
            mockRequest.setContent(docContent.getBytes());
            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(0, this.countRowsInTable(AUDIT_TABLE_NAME));
    }

    /**
     * Test that when multiple node refs are inserted into the POST data that the same number of events are persisted to
     * the database. This relates to the action where on the UI the user selects multiple documents then uses the
     * multi-copy action.
     */
    @Test
    public void testMultipleDocumentCopy() {

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
    public void testMultipleCopyFilesAndFolders() {
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
    public void testFolderCopy() {
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

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FOLDERNODEREF_STRING, audited.getNodeRef());
            assertEquals(true, audited.isSuccess());
            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
