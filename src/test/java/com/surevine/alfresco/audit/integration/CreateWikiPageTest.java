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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.CreateWikiPageAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class CreateWikiPageTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public CreateWikiPageTest() {
        super(new CreateWikiPageAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulCreation() {

        try {

            JSONObject json = new JSONObject();

            json.put(AlfrescoJSONKeys.PAGE, "wiki-page");
            json.put(AlfrescoJSONKeys.PAGETITLE, TEST_WIKIPAGE_NAME);
            json.put(AlfrescoJSONKeys.WIKI_PAGE_CONTENT, "<p>in europa league action.</p>");
            eslFixture.addToJSON(json);

            mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/" + TEST_WIKIPAGE_NAME);
            mockRequest.setContent(json.toString().getBytes());
            mockResponse = new MockHttpServletResponse();
            mockChain = new MockFilterChain();

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_WIKIPAGE_NAME, audited.getSource());
            assertEquals(eslFixture.toString(), audited.getSecLabel());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test to ensure that when the user attempts to create a wiki page with the same name as another wiki page that the
     * attempt is audited as unsuccessful.
     */
    @Test
    public void testDuplicateDocumentCreationAttempt() {

        String pageName = "duplicate";
        String failureDetails = "Request could not be completed due to a conflict with the current state of the resource.";

        try {

            JSONObject request = new JSONObject();

            request.put(AlfrescoJSONKeys.PAGE, "wiki-page");
            request.put(AlfrescoJSONKeys.PAGETITLE, pageName);
            request.put(AlfrescoJSONKeys.PAGE_CONTENT, "<p>duplicate.</p>");

            JSONObject response = new JSONObject();
            JSONObject status = new JSONObject();
            status.put(AlfrescoJSONKeys.CODE, HttpServletResponse.SC_CONFLICT);
            status.put(AlfrescoJSONKeys.NAME, "Conflict");
            status.put(AlfrescoJSONKeys.DESCRIPTION, failureDetails);
            response.put("status", status);

            mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/" + pageName);
            mockRequest.setMethod(cut.getMethod());
            mockRequest.setContent(request.toString().getBytes());
            mockResponse = new MockHttpServletResponse();


            mockChain = new ResponseModifiableMockFilterChain(response.toString(), HttpServletResponse.SC_CONFLICT);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            assertEquals(false, audited.isSuccess());
            assertEquals(HttpStatus.CONFLICT.toString() + ": " + HttpStatus.CONFLICT.name(), audited.getDetails());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
