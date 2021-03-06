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

import javax.servlet.http.HttpServletResponse;

import org.alfresco.service.cmr.repository.StoreRef;
import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.CreateDiscussionReplyAuditEventListener;

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
