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
import com.surevine.alfresco.audit.listeners.SearchSiteAuditEventListener;

/**
 * @author garethferrier
 * 
 */

public class SearchSiteTest extends AbstractAuditIntegrationTestBase {

    /**
     * 
     */
    public SearchSiteTest() {
        super(new SearchSiteAuditEventListener());
    }

    /**
     * Test the normal search scenario.
     */
    @Test
    public void testSuccessfulSearch() {
        String queryString = "What's the meaning of life";

        try {
            mockRequest.setRequestURI("/alfresco/s/slingshot/search");
            mockRequest.setParameter("term", queryString);
            mockRequest.setParameter("site", TEST_SITE);

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(queryString, audited.getSource());
            assertEquals("No label for search.", audited.getSecLabel());

        } catch (Exception e) {
            fail();
        }
    }
}
