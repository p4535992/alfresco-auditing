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


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.CopyDocumentAuditEventListener;
import com.surevine.alfresco.audit.listeners.CreateDiscussionAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class LargeInputFieldTest extends AbstractAuditIntegrationTestBase {

    /**
     * Default constructor.
     */
    public LargeInputFieldTest() {
        // Use any old event listener.
        super(new CopyDocumentAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testLargeNumberOfOpenGroups() {

        int boundaryNumberOfOpenGroups = 80;
        
        try {
            JSONObject json = new JSONObject();
            
            // Now add a number of open groups, beyond that which is available on site.
            for (int i = 0; i < boundaryNumberOfOpenGroups; i++) {
                eslFixture.addOpenGroup("OG" + i);                
            }

            eslFixture.addToJSON(json);
            
            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI("/alfresco/s/slingshot/doclib/action/copy-to/site/mytestsite/documentLibrary/folder%20for%20copies/" + TEST_FILE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            // Only have to get the single event to confirm that it worked.
            Auditable audited = getSingleAuditedEvent();
            audited.getSecLabel();


        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    /**
     * Test that when a string longer than that expected by the database is inserted that it is dealt with.
     * 
     * This test is essentially a copy of the CreateDiscussion test, simply because it allows for an easy
     * way to extend the name of the site beyond that allowed by the column definition.
     */
    @Test
    public void testOverrunOfSiteColumnConstraints() {
        
        // Need to reset the CUT
        this.cut = new CreateDiscussionAuditEventListener();
        
        mockRequest.setRequestURI("/alfresco/s/api/forum/site/mytestsite/discussions/posts");
        String discussionTitle = "Title of discussion";

        int lengthOfSiteColumn = 80;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengthOfSiteColumn + 1; i++) {
            sb.append("x");
        }
        
        try {
            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.TOPIC, "");
            json.put(AlfrescoJSONKeys.SITE, sb.toString());
            json.put(AlfrescoJSONKeys.CONTAINER, "discussions");
            json.put(AlfrescoJSONKeys.PAGE, "discussions-topicview");

            json.put(AlfrescoJSONKeys.TITLE, discussionTitle);
            json.put(AlfrescoJSONKeys.CONTENT, "<p>The content of the discussion</p>");
            eslFixture.addToJSON(json);

            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable event = getSingleAuditedEvent();
            assertEquals(lengthOfSiteColumn, event.getSite().length());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
