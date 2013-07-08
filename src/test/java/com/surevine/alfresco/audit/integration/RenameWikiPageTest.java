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

import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.RenameWikiPageAuditEventListener;

/**
 * @author garethferrier
 *
 */
public class RenameWikiPageTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public RenameWikiPageTest() {
        super(new RenameWikiPageAuditEventListener());
    }

    /**
     * Test sunny day scenario. 
     */
    @Test
    public void testSuccess() {
        
        String rename = "Rename";
        try {

            // Create an empty JSON string for the post data.
            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.PAGE, "wiki-page");

            json.put(AlfrescoJSONKeys.NAME, rename);

            mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/" + TEST_WIKIPAGE_NAME);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getSingleAuditedEvent();

            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_WIKIPAGE_NAME, audited.getSource());
            assertEquals("Renamed to " + rename, audited.getDetails());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }
}
