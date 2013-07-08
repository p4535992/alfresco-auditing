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

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.DeleteDocumentCommentAuditEventListener;

/**
 * @author garethferrier
 *
 */

public class DeleteDocumentCommentTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public DeleteDocumentCommentTest() {
        super(new DeleteDocumentCommentAuditEventListener());
    }
    
    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulDeletion() {
        
        try {

            mockRequest
                    .setRequestURI("/alfresco/s/api/comment/node/workspace/SpacesStore/" + TEST_DOCUMENT_COMMENT_NODEREF_ID);
            
            mockRequest.setParameter(AlfrescoJSONKeys.ITEM_TITLE, TEST_FILE);
            mockRequest.setParameter(AlfrescoJSONKeys.SITE, TEST_SITE);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getSingleAuditedEvent();

            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());
            assertEquals(TEST_FILE, audited.getSource());
            verifyGenericAuditMetadata(audited);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }        
    }

}
