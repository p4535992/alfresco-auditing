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

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.UpdateDocumentMetadataAuditEventListener;

/**
 * @author garethferrier
 *
 */
public class UpdateDocumentMetadataTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public UpdateDocumentMetadataTest() {
        super(new UpdateDocumentMetadataAuditEventListener());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccess() {

        try {

            // Create an empty JSON string for the post data.
            JSONObject json = new JSONObject();
            json.put("prop_cm_name", "ant.properties");
            json.put("prop_cm_title", "ant.properties");
            json.put("prop_cm_description", "A description of the file.");
            json.put("prop_mimetype", "text/plain");
            json.put("prop_cm_author", "Gareth Ferrier");
            json.put("prop_cm_taggable", "");
            json.put("prop_cm_categories", "");

            mockRequest
                    .setRequestURI("/alfresco/s/api/node/workspace/SpacesStore/" + TEST_FILENODEREF_ID + "/formprocessor");
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getSingleAuditedEvent();
            verifyGenericAuditMetadata(audited);

            assertEquals(TEST_FILE, audited.getSource());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
    
    
    /**
     * There are two user actions that allow the changing of the metadata.
     * The first is tested above, from the details page, the second from the list page is tested here.
     */
    @Test
    public void testSuccessFromListView() {
        try {

            JSONObject properties = new JSONObject();
            properties.put(AlfrescoJSONKeys.DESCRIPTION, "description added");
            properties.put(AlfrescoJSONKeys.NAME, TEST_FILE);
            properties.put(AlfrescoJSONKeys.TITLE, "title added");
            
            JSONObject json = new JSONObject();
            json.put(AlfrescoJSONKeys.PROPERTIES, properties);
            json.put(AlfrescoJSONKeys.TAGS, "");

            mockRequest
                    .setRequestURI("/alfresco/s/api/metadata/node/workspace/SpacesStore/" + TEST_FILENODEREF_ID);
            mockRequest.setContent(json.toString().getBytes());

            springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);
            
            Auditable audited = getSingleAuditedEvent();
            assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());
            assertEquals(TEST_USER, audited.getUser());
            assertEquals(cut.getAction(), audited.getAction());

            assertEquals(TEST_FILE, audited.getSource());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
