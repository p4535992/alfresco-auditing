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

import java.io.Serializable;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.web.app.servlet.AuthenticationHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.SpringAuditFilterBean;
import com.surevine.alfresco.audit.integration.stubs.StubSessionUser;
import com.surevine.alfresco.audit.listeners.AuditEventListener;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * @author garethferrier
 * 
 */
@SuppressWarnings("deprecation")
public class AbstractAuditIntegrationTestBase extends AbstractDependencyInjectionSpringContextTests {

    private static final String WORKSPACE_PROTOCOL = "workspace://SpacesStore/";

    /**
     * Name for the user in the tests.
     */
    public static final String TEST_USER = "Joseph Bloggs";

    /**
     * String for the name of the table where audit events are recorded.
     */
    public static final String AUDIT_TABLE_NAME = "alf_accounting_audit";

    public static final String TEST_FILENODEREF_ID = "11111111-1111-1111-1111-111111111111";
    /**
     * Correctly formatted nodeRef string that will be interpreted as a file.
     */
    public static final String TEST_FILENODEREF_STRING = WORKSPACE_PROTOCOL + TEST_FILENODEREF_ID;
    public static final String TEST_VERSIONED_FILENODEREF_STRING = "workspace://version2Store/" + TEST_FILENODEREF_ID;
    
    public static final String TEST_FOLDERNODEREF_ID = "99999999-9999-9999-9999-999999999999";
    
    public static final String TEST_FOLDER_PATH = "@{http://www.alfresco.org/model/application/1.0}company_home/@{http://www.alfresco.org/model/site/1.0}sites/@{http://www.alfresco.org/model/content/1.0}mytestsite/@{http://www.alfresco.org/model/content/1.0}documentLibrary/@{http://www.alfresco.org/model/c";
    
    /**
     * Correctly formatted nodeRef string that will be interpreted as a foler.
     */
    public static final String TEST_FOLDERNODEREF_STRING = WORKSPACE_PROTOCOL + TEST_FOLDERNODEREF_ID;
    
    /**
     * Name for the site used in the tests.
     */
    public static final String TEST_SITE = "mytestsite";

    /**
     * IP address of the remote address that will be resolved when running the integration tests.
     */
    public static final String TEST_REMOTE_ADDR = "127.0.0.1";

    public static final String TEST_FOLDER = "MyDocuments";
    
    public static final String NESTED_TEST_FOLDER = "/nest one/nest two";
    /**
     * Fixture of the filename used in the tests.
     */
    public static final String TEST_FILE = "testFile.txt";

    public static final Serializable TEST_VERSION_LABEL = "9.99";

    protected static final String SECLABEL_UNCHANGED = "unchanged";

    public static final String MIME_LINE_DELIMITER ="\r\n";
    
    /**
     * Helper method to construct the contents of the mime type from input test data.
     * 
     * @param testData String[][] containing the form-data keys and values.
     * @param filename of the file that is being uploaded
     * @param boundary the value used to the next type
     * @return
     */
    protected static String formatMimeType(String[][] testData, String filename, String boundary) {
    
        StringBuilder sb = new StringBuilder();
        
        // Start the string off
        sb.append(boundary + MIME_LINE_DELIMITER);
        sb.append(MIME_LINE_DELIMITER);
        
        for (int i = 0; i < testData.length; i++) {
            if (testData[i][0].contains("filename")) {
                sb.append(testData[i][0] + testData[i][1] + MIME_LINE_DELIMITER);
                sb.append("Content-Type: application/octet-stream" + MIME_LINE_DELIMITER);
                sb.append(MIME_LINE_DELIMITER);
                sb.append("content" + MIME_LINE_DELIMITER);
                sb.append(MIME_LINE_DELIMITER);
                sb.append(boundary + MIME_LINE_DELIMITER);                
            } else {
                sb.append(testData[i][0] + MIME_LINE_DELIMITER);
                sb.append(MIME_LINE_DELIMITER);
                sb.append(testData[i][1] + MIME_LINE_DELIMITER);
                sb.append(boundary + MIME_LINE_DELIMITER);                
            }
        }
        
        // Add the file name
        sb.append("Content-Disposition: form-data; name=\"Upload\"" + MIME_LINE_DELIMITER);
        sb.append(MIME_LINE_DELIMITER);
        sb.append("Submit Query" + MIME_LINE_DELIMITER);
        // Now end the string
        sb.append(boundary + "--");
        
        return sb.toString();
    }

    protected SpringAuditFilterBean springAuditFilterBean;

    @Override
    protected String[] getConfigLocations() {
        String[] config = { "com/surevine/alfresco/audit/testApplicationContext.xml" };
        return config;
    }

    protected JdbcTemplate jdbcTemplate;

    protected MockHttpServletRequest mockRequest;

    protected MockHttpServletResponse mockResponse;
    protected FilterChain mockChain;

    protected AuditEventListener cut;

    private Connection connection;

    private Statement statement;
    
    protected EnhancedSecurityLabel eslFixture;
    
    protected EnhancedSecurityLabel emptyESL;

    /**
     * Constructor.
     * 
     * @param cut
     *            ClassUnderTest
     */
    public AbstractAuditIntegrationTestBase(final AuditEventListener cut) {
        this.cut = cut;

        initialiseFixtures();
    }
    
    public static final String TEST_NATIONALITY_OWNER = "SV";
    public static final String TEST_PROTECTIVE_MARKING = "NATO RESTRICTED";
    public static final String TEST_OPEN_GROUP1 = "BREAKINGTRAIL";
    public static final String TEST_OPEN_GROUP2 = "CLIENT1";
    public static final String TEST_CLOSED_GROUP1 = "COMMERCIAL";
    public static final String TEST_CLOSED_GROUP2 = "HR";
    public static final String TEST_NATIONALITY_CAVEATS = "UK EYES ONLY";
    public static final String TEST_FREEFORM_CAVEAT = "NO MORE";
    public static final String TEST_ORGANISATION1 = "ORG1";
    public static final String TEST_ORGANISATION2 = "ORG2";

    public static final String FORM_DATA_CONTENT = "Content-Disposition: form-data; ";

    public static final String TEST_WIKIPAGE_NODEREF_ID = "22222222-2222-2222-2222-2222222222222";
    public static final String TEST_WIKIPAGE_NODEREF_STRING = WORKSPACE_PROTOCOL + TEST_WIKIPAGE_NODEREF_ID;
    public static final String TEST_WIKIPAGE_NAME = "My_Wiki_Page";
    
    // Just needs to be different from the normal wiki page.
    public static final String TEST_VERSIONED_WIKIPAGE_NODEREF_ID = "22222222-9999-9999-9999-2222222222222";
    public static final String TEST_VERSIONED_WIKIPAGE_NODEREF_STRING = "versionStore://version2Store/" + TEST_VERSIONED_WIKIPAGE_NODEREF_ID;
    public static final String TEST_VERSIONED_WIKIPAGE_VERSION ="2.0";

    public static final String TEST_DISCUSSION_TOPIC_NODEREF_ID = "33333333-3333-3333-3333-3333333333333";
    public static final String TEST_DISCUSSION_TOPIC_NODEREF_STRING = WORKSPACE_PROTOCOL + TEST_DISCUSSION_TOPIC_NODEREF_ID;
    public static final String TEST_DISCUSSION_TOPIC_NAME = "post-1288169634961_839";

    public static final String TEST_DISCUSSION_REPLY_NODEREF_ID = "44444444-4444-4444-4444-444444444444";
    public static final String TEST_DISCUSSION_REPLY_NODEREF_STRING = WORKSPACE_PROTOCOL + TEST_DISCUSSION_REPLY_NODEREF_ID;
    public static final String TEST_DISCUSSION_REPLY_NAME = TEST_DISCUSSION_REPLY_NODEREF_ID;
    
    public static final String TEST_DOCUMENT_COMMENT_NODEREF_ID = "55555555-5555-5555-5555-555555555555";
    public static final String TEST_DOCUMENT_COMMENT_NODEREF_STRING = WORKSPACE_PROTOCOL + TEST_DOCUMENT_COMMENT_NODEREF_ID;

    protected void initialiseFixtures() {
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        mockChain = new MockFilterChain();
        
        mockRequest.setMethod(cut.getMethod());

        // Set reasonable defaults.
        mockRequest.setProtocol("http");
        mockRequest.setServerName("localhost");
        mockResponse.setStatus(HttpServletResponse.SC_OK);

        mockRequest.setUserPrincipal(new Principal() {

            public String getName() {
                return TEST_USER;
            }
        });
        
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(AuthenticationHelper.AUTHENTICATION_USER, new StubSessionUser());
        mockRequest.setSession(session);
        
        // Set up the security label fixture
        // Setup the security label 
        eslFixture = new EnhancedSecurityLabel(TEST_PROTECTIVE_MARKING);

        eslFixture.setNationalityOwner(TEST_NATIONALITY_OWNER);
        eslFixture.setNationalityCaveats(TEST_NATIONALITY_CAVEATS);
        eslFixture.setCaveat(TEST_FREEFORM_CAVEAT);
        eslFixture.addOpenGroup(TEST_OPEN_GROUP1);
        eslFixture.addOpenGroup(TEST_OPEN_GROUP2);
        eslFixture.addClosedGroup(TEST_CLOSED_GROUP1);
        eslFixture.addClosedGroup(TEST_CLOSED_GROUP2);
        eslFixture.addOrganisation(TEST_ORGANISATION1);
        eslFixture.addOrganisation(TEST_ORGANISATION2);
        
        // Initialise the empty ESL
        emptyESL = new EnhancedSecurityLabel();       
    }


    /**
     * Used to query for specific events of the type of the ClassUnderTest.
     * 
     * @return
     */
    protected String getSimpleQuery() {
        return "select * from " + AUDIT_TABLE_NAME + " where action = '" + cut.getAction() + "' ORDER BY tstamp ASC;";
    }

    /**
     * Used to query for all events.
     * 
     * @return
     */
    protected String getAllEventsQuery() {
        return "select * from " + AUDIT_TABLE_NAME + " ORDER BY tstamp ASC;";
    }

    /**
     * @return
     * @throws Exception 
     */
    protected Auditable getSingleAuditedEvent() throws Exception {
        @SuppressWarnings("unchecked")
        List<Auditable> audits = jdbcTemplate.query(getSimpleQuery(), new AuditRowMapper());
        if (audits.size() == 1) {
            return audits.get(0);
        }
        
        throw new Exception("Invalid number of events: "+audits.size());
    }

    /**
     * Gets a list of all the audit events so far
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    protected List<Auditable> getAllAuditEvents() throws Exception {
        return jdbcTemplate.query(getAllEventsQuery(), new AuditRowMapper());
    }

    protected void verifyGenericAuditMetadata(final Auditable audited, final String action) {
        assertEquals(TEST_REMOTE_ADDR, audited.getRemoteAddress());
        assertEquals(TEST_USER, audited.getUser());
        assertEquals(action, audited.getAction());
        
        // the security label should never be null, nor should it be equal to 'unchanged' INT-395.
        assertNotNull(audited.getSecLabel());
        assertNotSame(SECLABEL_UNCHANGED, audited.getSecLabel());
        assertNotNull(audited.getVersion());
        assertTrue(audited.isSuccess());
        assertNotNull(audited.getNodeRef());
    }

    protected void verifyGenericAuditMetadata(final Auditable audited) {
        verifyGenericAuditMetadata(audited, cut.getAction());
    }

    /**
     * This method has been added as there is support for this method in spring 3.0.0 and not in spring 2.0.8.
     * 
     * @param tableName
     * @return
     */
    protected int countRowsInTable(final String tableName) {

        return jdbcTemplate.queryForInt("select count(*) from " + tableName + ";");
    }

    /**
     * This method has been added as there is support for this method in spring 3.0.0 and not in spring 2.0.8.
     * 
     * @param tableName
     * @return
     */
    protected void deleteFromTables(final String tableName) {
        jdbcTemplate.execute("truncate table " + tableName + ";");
    }

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();

        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:mem:test";
        String username = "sa";
        String password = "";

        connection = DriverManager.getConnection(url, username, password);

        statement = connection.createStatement();
        try {
            statement.execute("delete from " + AUDIT_TABLE_NAME + ";");
        } catch (SQLException ex) { // if no table then create one.
            ex.printStackTrace();
            statement.execute("create table " + AUDIT_TABLE_NAME + " (" + " id INTEGER IDENTITY primary key,"
                    + "tstamp TIMESTAMP," + "username varchar(40)," + "action varchar(40)," + "source varchar(80),"
                    + "secLabel varchar(1024)," + "details varchar(256)," + "success varchar(10)," + "site varchar(80),"
                    + "url varchar(256)," + "version varchar(10)," + "remote_addr varchar(40)," + "tags varchar(256),"
                    + "node_ref varchar(80),"+ "time_spent BIGINT);");
        }

        try {
            statement.execute("select count(*) from alf_accounting_audit;");

        } catch (SQLException ex) {
            ex.printStackTrace();
            fail();
        }

        // Initialise the local beans from the application context.
        springAuditFilterBean = (SpringAuditFilterBean) applicationContext.getBean("auditFilter");
        jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
    }

    @Override
    protected void onTearDown() throws Exception {
        super.onTearDown();

        try {
            // To ensure that no follow on tests are effected by this test truncate the table.
            // Previously we were shutting down the database and closing the connection, but for
            // some reason that doesn't work with multiple tests.
            statement.execute("truncate table " + AUDIT_TABLE_NAME + ";");
        } catch (Exception ex) {
        }

    }
}
