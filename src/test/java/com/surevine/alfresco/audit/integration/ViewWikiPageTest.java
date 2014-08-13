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

import org.junit.Ignore;
import org.junit.Test;

import com.surevine.alfresco.audit.Auditable;

import com.surevine.alfresco.audit.listeners.AbstractAuditEventListener;
import com.surevine.alfresco.audit.listeners.ViewWikiPageAuditEventListener;

/**
 * @author garethferrier
 * 
 */
public class ViewWikiPageTest extends AbstractAuditIntegrationTestBase {

    /**
     * Constructor.
     */
    public ViewWikiPageTest() {
        super(new ViewWikiPageAuditEventListener());
    }

    /**
     * This one came up on a number of times in the reference environment.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFalsePositive() throws Exception {
        // This URL has in the past been a source of false positive audit events.
        mockRequest.setRequestURI("/alfresco/s/remotestore/has/alfresco/site-data/pages/wiki-page.xml");

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

        List<Auditable> audits = jdbcTemplate.query(getSimpleQuery(), new AuditRowMapper());

        assertEquals(0, audits.size());
    }

    /**
     * Test sunny day scenario.
     */
    @Test
    public void testSuccessfulView() throws Exception {
        mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/page/mytestsite/" + TEST_WIKIPAGE_NAME);

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

        Auditable audited = getSingleAuditedEvent();

        verifyGenericAuditMetadata(audited);
        assertEquals(TEST_WIKIPAGE_NAME, audited.getSource());
        assertNotNull(audited.getSecLabel());
        assertNotSame(SECLABEL_UNCHANGED, audited.getSecLabel());

    }

    /**
     * Test that versioned access to a wiki page is audited.
     */
    @Test
    public void testVersionedWikiPage() throws Exception {
        mockRequest.setRequestURI("/alfresco/s/slingshot/wiki/version/mytestsite/" + TEST_WIKIPAGE_NAME + "/"
                + TEST_VERSIONED_WIKIPAGE_NODEREF_ID);

        springAuditFilterBean.doFilter(mockRequest, mockResponse, mockChain);

        Auditable audited = getSingleAuditedEvent();
        verifyGenericAuditMetadata(audited);
        assertEquals(TEST_VERSIONED_WIKIPAGE_VERSION, audited.getVersion());
        assertEquals(AbstractAuditEventListener.NO_SECURITY_LABEL, audited.getSecLabel());

    }
    
    @Test
    public void testWikiPageListViewNotAudited() {
        String wikiPageListURI = "/alfresco/wcs/slingshot/wiki/pages/sandbox";
        mockRequest.setRequestURI(wikiPageListURI);
        
        ViewWikiPageAuditEventListener viewWikiPageListener = (ViewWikiPageAuditEventListener)applicationContext.getBean("viewWikiPageAuditEventListener");
        
        assertFalse("View of wiki main page incorrectly dealt with", viewWikiPageListener.isEventFired(mockRequest));
    }
}
