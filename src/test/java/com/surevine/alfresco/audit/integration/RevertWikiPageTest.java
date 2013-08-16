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
