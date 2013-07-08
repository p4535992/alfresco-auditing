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

import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.DeleteDiscussionAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class DeleteDiscussionTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public DeleteDiscussionTest() {
        super(new DeleteDiscussionAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulDeletion() {

        String postId = "post-1288169634961_839";
        mockRequest.setParameter("page", "discussions-topicview");
        mockRequest.setRequestURI("/alfresco/s/api/forum/post/site/mytestsite/discussions/" + postId);

        try {

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(postId, audited.getSource());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
