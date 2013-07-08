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

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.CreateDiscussionAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class CreateDiscussionTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public CreateDiscussionTest() {
        super(new CreateDiscussionAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulCreation() {
        String requestURI = "/alfresco/s/api/forum/site/mytestsite/discussions/posts";
        mockRequest.setRequestURI(requestURI);
        String discussionTitle = "Title of discussion";
        
        try {
            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.TOPIC, "");
            json.put(AlfrescoJSONKeys.SITE, TEST_SITE);
            json.put(AlfrescoJSONKeys.CONTAINER, "discussions");
            json.put(AlfrescoJSONKeys.PAGE, "discussions-topicview");

            json.put(AlfrescoJSONKeys.TITLE, discussionTitle);
            json.put(AlfrescoJSONKeys.CONTENT, "<p>The content of the discussion</p>");
            eslFixture.addToJSON(json);

            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            List<Auditable> events = getAllAuditEvents();
            
            assertEquals("There should be one audit events", 1, events.size());
            
            // The first event should be the CREATE_DISCUSSION event
            Auditable audited = events.get(0);
            
            verifyGenericAuditMetadata(audited);        
            assertEquals(discussionTitle, audited.getSource());
            assertEquals(eslFixture.toString(), audited.getSecLabel().toString());
            assertEquals(requestURI, audited.getDetails());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
