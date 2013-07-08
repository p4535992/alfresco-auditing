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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.MultiDocumentDeleteAuditEventListener;

/**
 * @author garethferrier
 *
 */
public class MultiDocumentDeleteTest extends AbstractAuditIntegrationTestBase {

    public static final String REQUEST_URI_FIXTURE = "/alfresco/s/slingshot/doclib/action/files";
    
    /**
     * 
     */
    public MultiDocumentDeleteTest() {
        super(new MultiDocumentDeleteAuditEventListener());
    }
    
    /**
     * Test that multi-document deletion ends up with the correct number of auditable events.
     */
    @Test
    public void testSuccessfulDeletion() {
        
        try {
            
            JSONObject json = new JSONObject();

            eslFixture.addToJSON(json);

            JSONArray arr = new JSONArray();
            arr.put(TEST_FILENODEREF_STRING);
            
            arr.put("workspace://SpacesStore/12345678-1234-1234-1234-123456789012");
            json.put(AlfrescoJSONKeys.NODEREFS, arr);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.addParameter("alf_method", "delete");
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            // Now check that two events have been created.
            assertEquals(2, countRowsInTable(AUDIT_TABLE_NAME));
            
        } catch (Exception e) {
            fail();
        }
    }

}
