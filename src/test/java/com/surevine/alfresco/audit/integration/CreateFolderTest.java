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
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.CreateFolderAuditEventListener;

/**
 * @author garethferrier
 *
 */

public class CreateFolderTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public CreateFolderTest() {
        super(new CreateFolderAuditEventListener());
    }
    
    /**
     * Test successful creation of a folder.
     */
    @Test
    public void testSuccessfulCreation() {
        String testFolderName = "test folder";
        
        try {

            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.CM_DESCRIPTION, "A folder to contain resources");
            json.put(AlfrescoJSONKeys.CM_FOLDER_NAME, testFolderName);
            json.put(AlfrescoJSONKeys.CM_TITLE, "the title of the test folder");
            json.put(AlfrescoJSONKeys.DEST_FOLDER, "workspace://SpacesStore/059776cf-4c36-47c9-a357-4109cb137cf6");
            
            mockRequest.setContent(json.toString().getBytes());
            mockRequest
                    .setRequestURI("/alfresco/wcs/api/type/cm_folder/formprocessor");

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getSingleAuditedEvent();

            assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());
            verifyGenericAuditMetadata(audited);
            // TODO Can't get site name from node ref alone.
            assertEquals(TEST_SITE, audited.getSite());
            assertEquals(TEST_USER, audited.getUser());
            assertEquals(cut.getAction(), audited.getAction());
            assertEquals(testFolderName, audited.getSource());
            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
