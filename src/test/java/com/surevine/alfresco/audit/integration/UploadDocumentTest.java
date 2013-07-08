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

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UploadDocumentAuditEventListener;

/**
 * Test class for the uploading of documents.
 * 
 * @author garethferrier
 * 
 */
public class UploadDocumentTest extends AbstractAuditIntegrationTestBase {

    private static final String MIME_BOUNDARY_PARAMETER = "----------Ef1GI3Ij5ei4ei4Ef1ei4KM7Ef1cH2";

    private static final String DESTINATION_DIRECTORY = "/nest one/nest two";
    /**
     * The boundary is defined as a line containing two hyphens followed by the boundary parameter from the Content-Type
     * field. See http://www.w3.org/Protocols/rfc1341/7_2_Multipart.html for a full discussion on the make up of the
     * protocol.
     */
    private static final String MIME_BOUNDARY = "--" + MIME_BOUNDARY_PARAMETER;

    /**
     * Constructor.
     */
    public UploadDocumentTest() {
        super(new UploadDocumentAuditEventListener());
    }

    public String[][] flashBasedUploadData = { { FORM_DATA_CONTENT + "name=\"Filename\"", TEST_FILE },
            { FORM_DATA_CONTENT + "name=\"containerId\"", "documentLibrary" },
            { FORM_DATA_CONTENT + "name=\"contentType\"", "cm:content" },
            { FORM_DATA_CONTENT + "name=\"siteId\"", TEST_SITE }, { FORM_DATA_CONTENT + "name=\"username\"", "null" },
            { FORM_DATA_CONTENT + "name=\"overwrite\"", "false" },
            { FORM_DATA_CONTENT + "name=\"eslOpenGroupsHidden\"", "BREAKINGTRAIL,CLIENT1" },
            { FORM_DATA_CONTENT + "name=\"eslOrganisationsHidden\"", "ORG1,ORG2" },
            { FORM_DATA_CONTENT + "name=\"eslatomal\"", "" },
            { FORM_DATA_CONTENT + "name=\"eslclosedgroupshidden\"", "COMMERCIAL,HR" },
            { FORM_DATA_CONTENT + "name=\"eslprotectivemarking\"", "NATO RESTRICTED" },
            { FORM_DATA_CONTENT + "name=\"eslcaveats\"", "NO MORE" },
            { FORM_DATA_CONTENT + "name=\"esleyes\"", "UK EYES ONLY" },
            { FORM_DATA_CONTENT + "name=\"eslnationalowner\"", "SV" },
            { FORM_DATA_CONTENT + "name=\"thumbnails\"", "doclib" },
            { FORM_DATA_CONTENT + "name=\"filedata\"; filename=\"", TEST_FILE + "\"" },
            { FORM_DATA_CONTENT + "name=\"uploadDirectory\"", DESTINATION_DIRECTORY } };

    public String[][] nonFlashBasedUploadData = {
            { FORM_DATA_CONTENT + "name=\"siteId\"", TEST_SITE },
            { FORM_DATA_CONTENT + "name=\"containerId\"", "documentLibrary" },
            { FORM_DATA_CONTENT + "name=\"username\"", "null" },
            { FORM_DATA_CONTENT + "name=\"updateNodeRef\"", "null" },
            { FORM_DATA_CONTENT + "name=\"uploadDirectory\"", DESTINATION_DIRECTORY },
            { FORM_DATA_CONTENT + "name=\"overwrite\"", "false" },
            { FORM_DATA_CONTENT + "name=\"thumbnails\"", "doclib" },
            { FORM_DATA_CONTENT + "name=\"successCallback\"",
                    "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_documentlibrary').onUploadSuccess" },
            { FORM_DATA_CONTENT + "name=\"successScope\"",
                    "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_documentlibrary')" },
            { FORM_DATA_CONTENT + "name=\"failureCallback\"",
                    "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_documentlibrary').onUploadFailure" },
            { FORM_DATA_CONTENT + "name=\"failureScope\"",
                    "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_documentlibrary')" },
            { FORM_DATA_CONTENT + "name=\"eslOpenGroupsHidden\"", "BREAKINGTRAIL,CLIENT1" },
            { FORM_DATA_CONTENT + "name=\"eslOrganisationsHidden\"", "ORG1,ORG2" },
            { FORM_DATA_CONTENT + "name=\"eslClosedGroupsHidden\"", "COMMERCIAL,HR" },
            { FORM_DATA_CONTENT + "name=\"eslNationalOwner\"", "SV" },
            { FORM_DATA_CONTENT + "name=\"eslProtectiveMarking\"", "NATO RESTRICTED" },
            { FORM_DATA_CONTENT + "name=\"eslAtomal\"", "" },
            { FORM_DATA_CONTENT + "name=\"eslCaveats\"", "NO MORE" },
            { FORM_DATA_CONTENT + "name=\"eslNationalCaveats\"", "UK EYES ONLY" },
            { FORM_DATA_CONTENT + "name=\"contentType\"", "cm:content" },
            { FORM_DATA_CONTENT + "name=\"filedata\"; filename=\"", TEST_FILE + "\"" },
            { FORM_DATA_CONTENT + "name=\"majorVersion\"", "false" },
            { FORM_DATA_CONTENT + "name=\"description\"", "" }, };

    /**
     * Test that when using a flash based upload that the upload is audited correctly.
     */
    @Test
    public void testFlashSuccess() {

        try {
            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(flashBasedUploadData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI("/alfresco/s/api/upload");

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(eslFixture.toString(), audited.getSecLabel());
            assertEquals(DESTINATION_DIRECTORY, audited.getDetails());
            assertTrue(audited.isSuccess());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Test that when using a non-flash based upload that the upload is audited correctly.
     */
    @Test
    public void testNonFlashSuccess() {

        try {
            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(nonFlashBasedUploadData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI("/alfresco/s/api/upload");

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(eslFixture.toString(), audited.getSecLabel());
            assertEquals(DESTINATION_DIRECTORY, audited.getDetails());
            assertTrue(audited.isSuccess());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    private static String incrementedTestFilename = "testFile-1.txt";

    /**
     * This test should deal with the scenario where a document is being uploaded which already has a file with the same
     * name in the target directory. Alfresco identifies this and creates the new file with '-<one_up_number>' appended
     * to the file name prior to the file extension. Quite a bit of JSON will need to be inserted into the response to
     * allow this test to be exercised.
     */
    @Test
    public void testFlashNameClashChangedByAlfresco() {

        try {
            // Setup the request object.
            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(flashBasedUploadData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI("/alfresco/s/api/upload");

            // Setup the response object.
            JSONObject response = new JSONObject();
            response.put(AlfrescoJSONKeys.NODEREF, TEST_FILENODEREF_STRING);
            response.put("fileName", incrementedTestFilename);
            JSONObject status = new JSONObject();
            status.put("code", HttpServletResponse.SC_OK);
            status.put("name", "OK");
            status.put("description", "File uploaded successfully");
            response.put("status", status);

            mockResponse = new MockHttpServletResponse();

            mockChain = new ResponseModifiableMockFilterChain(response.toString(), HttpServletResponse.SC_CONFLICT);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(incrementedTestFilename, audited.getSource());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    private static final String htmlUploadSuccessStart = "<html>" + MIME_LINE_DELIMITER
         + "<head>" + MIME_LINE_DELIMITER
         + "<title>Upload success</title>" + MIME_LINE_DELIMITER
         + "</head>" + MIME_LINE_DELIMITER
         + "<body>" + MIME_LINE_DELIMITER
         + "<script type=\"text/javascript\">" + MIME_LINE_DELIMITER
         + "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_documentlibrary').onUploadSuccess.call(window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_documentlibrary')," + MIME_LINE_DELIMITER;
    
   private static final String htmlUploadSuccessFinish = ");" + MIME_LINE_DELIMITER
         + "</script>" + MIME_LINE_DELIMITER
         + "</body>" + MIME_LINE_DELIMITER
         + "</html>" + MIME_LINE_DELIMITER;
    
    /**
     * This test deals with the scenario where a file is uploaded using a the non-flash based uploader
     * and the target directory already has a visible file of the same name.  Need to be able to insert
     * HTML containing JSON into the response so that it the updated filename can be extracted.
     */
    @Test
    public void testNonFlashNameClashChangedByAlfresco() {

        try {
            // Setup the request object.
            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(nonFlashBasedUploadData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI("/alfresco/s/api/upload");

            mockResponse = new MockHttpServletResponse();
            
            // Setup the response object.
            JSONObject response = new JSONObject();
            response.put(AlfrescoJSONKeys.NODEREF, TEST_FILENODEREF_STRING);
            response.put("fileName", incrementedTestFilename);
            JSONObject status = new JSONObject();
            status.put("code", HttpServletResponse.SC_OK);
            status.put("name", "OK");
            status.put("description", "File uploaded successfully");
            response.put("status", status);

            mockChain = new ResponseModifiableMockFilterChain(
                    htmlUploadSuccessStart + response.toString() + htmlUploadSuccessFinish, HttpServletResponse.SC_CONFLICT);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(incrementedTestFilename, audited.getSource());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Under certain circumstances alfresco will not implement a name change. One instance is where the source of the
     * name clash is not visible to the user, this is the case where they don't have permissions. If this is the case
     * alfresco responds with a 409 (HttpServletResponse.SC_CONFLICT)
     */
    @Test
    public void testNameClashNotChangedByALfresco() {

        try {
            // Setup the request object.
            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(nonFlashBasedUploadData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI("/alfresco/s/api/upload");

            // Setup the response object.
            mockResponse = new MockHttpServletResponse();

            // There is no content to the response so only an empty string is required
            mockChain = new ResponseModifiableMockFilterChain("{}", HttpServletResponse.SC_CONFLICT);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertFalse(audited.isSuccess());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
