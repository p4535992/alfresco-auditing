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
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.CreateWikiPageAuditEventListener;

/**
 * Test class to ensure that correct support is implemented for the auditing of events
 * where the set of tags is changed.  Will need to use one of the concrete listeners for
 * the base of the test, so there will be some duplication, but it is unavoidable.
 * 
 * @author garethferrier
 *
 */
public class TagSupportTest extends AbstractAuditIntegrationTestBase {

    private String[] tags = {"current affairs", "politics", "Apostrophe's", "UNDER_SCORE"};
    
    /**
     * Constructor - use the 'CreateWikiPageAuditEventListener'.
     */
    public TagSupportTest() {
        super(new CreateWikiPageAuditEventListener());
    }
    /**
     * Test that during creation of a resource the tags are correctly reported.
     */
    public void testAdditionOfTagsAtCreationTime() {
        
        String pageName = "Watchin the pool";
        try {

            JSONObject json = new JSONObject();

            json.put(AlfrescoJSONKeys.PAGE, "wiki-page");
            json.put(AlfrescoJSONKeys.PAGETITLE, pageName);
            json.put(AlfrescoJSONKeys.WIKI_PAGE_CONTENT, "<p>in europa league action.</p>");
            eslFixture.addToJSON(json);
            JSONArray jsonTags = new JSONArray();

            // Add each of the fixture tags.
            StringBuffer expectedStringBuffer = new StringBuffer();
            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                jsonTags.put(tag);
                
                // Now populate the expected string as we go.
                expectedStringBuffer.append(tag);
                if (i != tags.length) {
                    expectedStringBuffer.append(", ");
                }
            }
            json.put(AlfrescoJSONKeys.TAGS, jsonTags);

            mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/Watchin_the_pool");
            mockRequest.setContent(json.toString().getBytes());
            mockResponse = new MockHttpServletResponse();
            mockChain = new MockFilterChain();

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            // Now only verify the presence of the tags that were added earlier
            assertEquals(expectedStringBuffer.toString(), audited.getTags());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
