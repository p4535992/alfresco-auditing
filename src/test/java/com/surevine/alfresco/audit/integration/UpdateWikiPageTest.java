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

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UpdateWikiPageAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class UpdateWikiPageTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public UpdateWikiPageTest() {
        super(new UpdateWikiPageAuditEventListener());
    }

    /**
     * Test sucessful scenario.
     */
    @Test
    public void testSuccess() {

        try {

            JSONObject json = new JSONObject();

            json.put(AlfrescoJSONKeys.CONTEXT, "/share/page/site/mytestsite/wiki-page?title=" + TEST_WIKIPAGE_NAME);
            json.put(AlfrescoJSONKeys.PAGE, "wiki-page");
            json.put(AlfrescoJSONKeys.CURRENT_VERSION, "1.2");
            json.put(AlfrescoJSONKeys.WIKI_PAGE_CONTENT, "<p>The contents of the page");
            json.put(AlfrescoJSONKeys.TAGS, "");
            eslFixture.addToJSON(json);

            mockRequest.setContent(json.toString().getBytes());
            mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/" + TEST_WIKIPAGE_NAME);

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
}
