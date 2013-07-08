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

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UpdateDocumentAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class UpdateDocumentTest extends AbstractAuditIntegrationTestBase {

    private static final String TARGET_URI = "/alfresco/s/api/upload";

    private static final String MIME_BOUNDARY_PARAMETER = "----------ae0ei4Ef1ae0ei4Ef1gL6Ef1ae0Ef1";
    
    private static final String MIME_BOUNDARY = "--" + MIME_BOUNDARY_PARAMETER;
    
    /**
     * Constructor.
     */
    public UpdateDocumentTest() {
        super(new UpdateDocumentAuditEventListener());
    }

    
    public String[][] flashBasedUpdateData = {
            {FORM_DATA_CONTENT + "name=\"Filename\"", TEST_FILE},
            {FORM_DATA_CONTENT + "name=\"siteId\"", TEST_SITE},
            {FORM_DATA_CONTENT + "name=\"description\"", "the description"},
            {FORM_DATA_CONTENT + "name=\"updateNodeRef\"", TEST_FILENODEREF_STRING},
            {FORM_DATA_CONTENT + "name=\"eslopengroupshidden\"", "BREAKINGTRAIL,CLIENT1"},
            {FORM_DATA_CONTENT + "name=\"eslclosedgroupshidden\"", "COMMERCIAL,HR"},
            {FORM_DATA_CONTENT + "name=\"eslorganisationshidden\"", "ORG1,ORG2"},
            {FORM_DATA_CONTENT + "name=\"majorVersion\"", "true"},
            {FORM_DATA_CONTENT + "name=\"eslEyes\"", "UK EYES ONLY"},
            {FORM_DATA_CONTENT + "name=\"username\"", "null"},
            {FORM_DATA_CONTENT + "name=\"eslcaveats\"", "NO MORE"},
            {FORM_DATA_CONTENT + "name=\"eslprotectivemarking\"", "NATO RESTRICTED"},
            {FORM_DATA_CONTENT + "name=\"eslnationalowner\"", "SV"},
            {FORM_DATA_CONTENT + "name=\"containerId\"", "documentLibrary"},
            {FORM_DATA_CONTENT + "name=\"filedata\"; filename=\"", TEST_FILE + "\""},
            {FORM_DATA_CONTENT + "name=\"uploadDirectory\"", "/"}
    };
    
    public String[][] nonFlashBasedUpdateData = {
            {FORM_DATA_CONTENT + "name=\"siteId\"", TEST_SITE},
            {FORM_DATA_CONTENT + "name=\"containerId\"", "documentLibrary"},
            {FORM_DATA_CONTENT + "name=\"username\"", "null"},
            {FORM_DATA_CONTENT + "name=\"updateNodeRef\"", TEST_FILENODEREF_STRING},
            {FORM_DATA_CONTENT + "name=\"uploadDirectory\"", "/"},
            {FORM_DATA_CONTENT + "name=\"overwrite\"", ""},
            {FORM_DATA_CONTENT + "name=\"thumbnails\"", ""},
            {FORM_DATA_CONTENT + "name=\"successCallback\"", "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_document-details').onUploadSuccess"},
            {FORM_DATA_CONTENT + "name=\"successScope\"", "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_document-details')"},
            {FORM_DATA_CONTENT + "name=\"failureCallback\"", "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_document-details').onUploadFailure"},
            {FORM_DATA_CONTENT + "name=\"failureScope\"", "window.parent.Alfresco.util.ComponentManager.get('template_x002e_html-upload_x002e_document-details')"},
            {FORM_DATA_CONTENT + "name=\"eslopengroupshidden\"", "BREAKINGTRAIL,CLIENT1"},            
            {FORM_DATA_CONTENT + "name=\"eslclosedgroupshidden\"", "COMMERCIAL,HR"}, 
            {FORM_DATA_CONTENT + "name=\"eslorganisationshidden\"", "ORG1,ORG2"},
            {FORM_DATA_CONTENT + "name=\"Filename\"", TEST_FILE},
            {FORM_DATA_CONTENT + "name=\"eslnationalowner\"", "SV"},
            {FORM_DATA_CONTENT + "name=\"eslprotectivemarking\"", "NATO RESTRICTED"},
            {FORM_DATA_CONTENT + "name=\"eslatomal\"", ""},
            {FORM_DATA_CONTENT + "name=\"eslcaveats\"", "NO MORE"},
            {FORM_DATA_CONTENT + "name=\"eslnationalcaveats\"", "UK EYES ONLY"},
            {FORM_DATA_CONTENT + "name=\"description\"", "the description"},
            {FORM_DATA_CONTENT + "name=\"filedata\"; filename=\"", TEST_FILE + "\""},
            {FORM_DATA_CONTENT + "name=\"majorVersion\"", "true"},
            {FORM_DATA_CONTENT + "name=\"description\"", "the description"}
    };


    /**
     * Test sunny day scenario when using a flash based uploader.
     */
    @Test
    public void testFlashBasedUpdate() {

        try {

            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(flashBasedUpdateData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI(TARGET_URI);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getAllAuditEvents().get(0);

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(eslFixture.toString(), audited.getSecLabel());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            assertNotNull(audited.getDetails());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testNonFlashBasedUpdate() {
        try {

            mockRequest.setContentType("multipart/form-data; boundary=" + MIME_BOUNDARY_PARAMETER);
            String mimeContent = formatMimeType(nonFlashBasedUpdateData, TEST_FILE, MIME_BOUNDARY);
            mockRequest.setContent(mimeContent.getBytes());
            mockRequest.setRequestURI(TARGET_URI);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(eslFixture.toString(), audited.getSecLabel());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());
            assertNotNull(audited.getDetails());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
