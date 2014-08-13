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

import java.util.List;

import org.json.JSONException;
import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.DeleteFolderAuditEventListener;

/**
 * @author garethferrier
 *
 */
public class DeleteFolderTest extends AbstractAuditIntegrationTestBase {

    private DeleteFolderAuditEventListener deleteFolderListener;
    private String requestURIString = "/alfresco/s/slingshot/doclib/action/file/node/workspace/SpacesStore/" + TEST_FOLDERNODEREF_ID;
    
    /**
     * Constructor.
     */
    public DeleteFolderTest() {
        super(new DeleteFolderAuditEventListener());
    }
    
    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        
        deleteFolderListener = (DeleteFolderAuditEventListener) applicationContext.getBean("deleteFolderAuditEventListener");
    }
    
    @Test
    public void testEventFired() {

        mockRequest.setRequestURI(requestURIString);
        assertTrue(deleteFolderListener.isEventFired(mockRequest));
    }
    
    @Test
    public void testEventPopulation() throws JSONException {
        
        mockRequest.setRequestURI(requestURIString);
        List<Auditable> events = deleteFolderListener.populateAuditItems(mockRequest, mockResponse);
        
        if (events.size() == 1) {
            // Now make sure that the type is a folder
            Auditable event = events.get(0);
            
            assertEquals(deleteFolderListener.getAction(), event.getAction());
            assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, event.getSecLabel());
        } else {
            fail();
        }
        
    }

}
