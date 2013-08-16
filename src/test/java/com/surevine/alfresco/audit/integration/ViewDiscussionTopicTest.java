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

import javax.servlet.FilterChain;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.ViewDiscussionTopicAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class ViewDiscussionTopicTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public ViewDiscussionTopicTest() {
        super(new ViewDiscussionTopicAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulViewing() {

        String discussionTopic = "post-1288169634961_839";
        try {

            mockRequest.setRequestURI("/alfresco/s/api/forum/post/site/mytestsite/discussions/" + discussionTopic);
            MockHttpServletResponse mockResponse = new MockHttpServletResponse();
            FilterChain mockChain = new MockFilterChain();

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(discussionTopic, audited.getSource());
            assertEquals(AbstractAuditEventListener.NO_VERSION_STRING, audited.getVersion());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }
}
