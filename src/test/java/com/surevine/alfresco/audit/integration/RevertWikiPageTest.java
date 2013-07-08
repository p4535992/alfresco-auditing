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
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.RevertWikiPageAuditEventListener;

/**
 * @author simonWhite
 * 
 */
public class RevertWikiPageTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public RevertWikiPageTest() {
        super(new RevertWikiPageAuditEventListener());
    }

    /**
     * Test sunny day scenario.  In fact, this is the only scenario as everything else is a CREATE
     */
    @Test
    public void testSuccessfulReversion() {

        try {

            JSONObject json = new JSONObject();

            json.put(AlfrescoJSONKeys.PAGE, "wiki-page");
            json.put(AlfrescoJSONKeys.WIKI_PAGE_CONTENT, "<p>in europa league action.</p>");
            eslFixture.addToJSON(json);

            mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/" + TEST_WIKIPAGE_NAME);
            mockRequest.setContent(json.toString().getBytes());
            mockResponse = new MockHttpServletResponse();
            mockChain = new MockFilterChain();

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(audited.getAction(), "WIKI_PAGE_REVERTED");
            assertEquals(eslFixture.toString(), audited.getSecLabel());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
