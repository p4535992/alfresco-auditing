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

import org.alfresco.service.cmr.repository.StoreRef;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.CreateDiscussionReplyAuditEventListener;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * @author garethferrier
 *
 */
public class CreateDiscussionReplyTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public CreateDiscussionReplyTest() {
        super(new CreateDiscussionReplyAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulCreation() {

        String requestURI = "/alfresco/s/api/forum/post/node/workspace/SpacesStore/" + TEST_DISCUSSION_TOPIC_NODEREF_ID + "/replies";
        
        mockRequest.setRequestURI(requestURI);

        try {
            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.CONTENT, "<p>here is the content of the discussion topic</p>");
            json.put(AlfrescoJSONKeys.SITE, TEST_SITE);
            json.put(AlfrescoJSONKeys.PAGE, "discussions-topicview");
            json.put(AlfrescoJSONKeys.CONTAINER, "discussions");
            
            JSONObject item = new JSONObject();
            item.put("url", "/forum/post/node/workspace/SpacesStore/" + TEST_DISCUSSION_REPLY_NODEREF_ID);
            item.put("repliesUrl", "/forum/post/node/workspace/SpacesStore/" + TEST_DISCUSSION_REPLY_NODEREF_ID + "/replies");
            item.put(AlfrescoJSONKeys.NODEREF, StoreRef.STORE_REF_WORKSPACE_SPACESSTORE.toString() + TEST_DISCUSSION_REPLY_NODEREF_ID);
            
            // Create a new JSON object for the response, which is where most of the data will come from.
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("item", item);

            mockRequest.setContent(json.toString().getBytes());

            // Now put the response JSON in the response object.
            mockChain = new ResponseModifiableMockFilterChain(responseJSON.toString(), HttpServletResponse.SC_OK);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();
            
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_DISCUSSION_REPLY_NODEREF_ID, audited.getSource());
            assertEquals(requestURI, audited.getUrl());
            assertEquals(TEST_SITE, audited.getSite());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
