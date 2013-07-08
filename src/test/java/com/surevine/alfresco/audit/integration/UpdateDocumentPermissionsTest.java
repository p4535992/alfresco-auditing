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
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UpdateDocumentPermissionsAuditEventListener;

/**
 * Test class for updating document permissions.
 * 
 * @author garethferrier
 * 
 */
public class UpdateDocumentPermissionsTest extends AbstractAuditIntegrationTestBase {

    private static final String REQUEST_URI_FIXTURE = "/alfresco/s/slingshot/doclib/action/permissions/set/site/mytestsite";
    private JSONArray permissionsArray;

    /**
     * Constructor.
     * 
     * @throws JSONException
     */
    public UpdateDocumentPermissionsTest() throws JSONException {
        super(new UpdateDocumentPermissionsAuditEventListener());

        String[][] stringPermissions = { { "GROUP_site_mytestsite_SiteContributor", "SiteContributor" },
                { "GROUP_site_mytestsite_SiteConsumer", "SiteContributor" },
                { "GROUP_site_mytestsite_SiteCollaborator", "SiteContributor" }, { "GROUP_EVERYONE", "SiteConsumer" } };

        // This is a bit of a pain but, the structure used to hold the permissions is pretty deep
        // so need to create it properly.
        JSONObject perm = new JSONObject();
        permissionsArray = new JSONArray();

        for (int i = 0; i < stringPermissions.length; i++) {
            perm.put(AlfrescoJSONKeys.GROUP, stringPermissions[i][0]);
            perm.put(AlfrescoJSONKeys.ROLE, stringPermissions[i][1]);
            permissionsArray.put(perm);
        }
    }

    /**
     * Test the sunny day scenario.
     */
    @Test
    public void testSuccess() {

        try {

            JSONArray nodes = new JSONArray();
            nodes.put(TEST_FILENODEREF_STRING);

            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.NODEREFS, nodes);
            json.put(AlfrescoJSONKeys.PERMISSIONS, permissionsArray);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            Auditable audited = getSingleAuditedEvent();
            verifyGenericAuditMetadata(audited);
            assertEquals(TEST_FILE, audited.getSource());
            assertEquals(TEST_FILENODEREF_STRING, audited.getNodeRef());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * The document library gives the user the chance to change multiple documents permissions, so we need to be able to
     * test to ensure that we handle that properly.
     */
    @Test
    public void testAdaptMultiplePermissions() {

        try {

            JSONArray nodes = new JSONArray();
            nodes.put(TEST_FILENODEREF_STRING);
            nodes.put("workspace://SpacesStore/22222222-2222-2222-2222-222222222222");

            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.NODEREFS, nodes);
            json.put(AlfrescoJSONKeys.PERMISSIONS, permissionsArray);

            mockRequest.setRequestURI(REQUEST_URI_FIXTURE);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

            assertEquals(2, countRowsInTable(AUDIT_TABLE_NAME));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
