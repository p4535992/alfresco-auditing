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
package com.surevine.esl;

import static com.surevine.alfresco.audit.integration.AbstractAuditIntegrationTestBase.*;
import static com.surevine.esl.EnhancedSecurityLabel.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.namespace.QName;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Junit test class for the EnhancedSecurityLabel POJO.
 * 
 * @author garethferrier
 * 
 */
public class EnhancedSecurityLabelTest {

    private static final String NATIONALITY_OWNER_FIXTURE = "UK";

    private static final String PROTECTIVE_MARKING_FIXTURE = "CONFIDENTIAL";

    private static final String NATIONALITY_CAVEATS_FIXTURE = "UK EYES";
    
    private static final String FREEFORM_TEXT_FIXTURE = "FREEFORM";

    private static final String EMPTY_STRING = "";

    private EnhancedSecurityLabel cut;

    private JSONObject validEslJSON = new JSONObject();
    
    private Map<QName, Serializable> propertiesFixture = new HashMap<QName, Serializable>();

    /**
     * Constructor, initialse as much as possible.
     * 
     * @throws JSONException
     */
    public EnhancedSecurityLabelTest() throws JSONException {
        validEslJSON.put(ATOMAL_JSON_STR, "ATOMAL1");
        validEslJSON.put(NATIONAL_CAVEATS_JSON_STR, "");
        validEslJSON.put(CLOSED_GROUPS_JSON_STR, "HR,COMMERCIAL");
        validEslJSON.put(OPEN_GROUPS_JSON_STR, "CLIENT1");
        validEslJSON.put(PROTECTIVE_MARKING_JSON_STR, PROTECTIVE_MARKING_FIXTURE);
        validEslJSON.put(NATIONALITY_OWNER_JSON_STR, NATIONALITY_OWNER_FIXTURE);
        validEslJSON.put(NATIONAL_CAVEATS_JSON_STR, NATIONALITY_CAVEATS_FIXTURE);
       
        ArrayList<String> openGroups = new ArrayList<String>();
        openGroups.add(TEST_OPEN_GROUP1);
        openGroups.add(TEST_OPEN_GROUP2);
        
        ArrayList<String> closedGroups = new ArrayList<String>();
        closedGroups.add(TEST_CLOSED_GROUP1);
        closedGroups.add(TEST_CLOSED_GROUP2);
        
        ArrayList<String> organisations = new ArrayList<String>();
        organisations.add(TEST_ORGANISATION1);
        organisations.add(TEST_ORGANISATION2);
        
        propertiesFixture.put(NATIONALITY_OWNER, TEST_NATIONALITY_OWNER);
        propertiesFixture.put(PROTECTIVE_MARKING, TEST_PROTECTIVE_MARKING);
        propertiesFixture.put(OPEN_GROUPS, openGroups);
        propertiesFixture.put(CLOSED_GROUPS, closedGroups);
        propertiesFixture.put(ORGANISATIONS, organisations);
        propertiesFixture.put(FREE_FORM_CAVEATS, TEST_FREEFORM_CAVEAT);
        propertiesFixture.put(NATIONALITY_CAVEATS, TEST_NATIONALITY_CAVEATS);
    }

    /**
     * There is a constructor which takes one mandatory field as an input parameter.
     */
    @Test
    public void testFailedUseOfValueConstructor() {


        // Test with an empty string as the protective marking.
        try {
            cut = new EnhancedSecurityLabel(EMPTY_STRING);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Test that given the correct input the ESL will be created successfully.
     */
    @Test
    public void testSuccessfulConstruction() {
        try {
            cut = new EnhancedSecurityLabel(PROTECTIVE_MARKING_FIXTURE);
            assertNotNull(cut);
        } catch (Exception e) {
            fail();
        }

    }

    /**
     * Test that given valid JSON an ESL will be successfully constructed.
     */
    @Test
    public void testSuccessfullyBuiltFromJSON() {
        try {
            cut = EnhancedSecurityLabel.buildLabel(validEslJSON);
            assertNotNull(cut);
        } catch (JSONException e) {
            fail();
        }

    }
    
    /**
     * test that given a string that doesn't contain an ESL that it doesn't complain, 
     * as this is a valid usage pattern.
     */
    @Test
    public void testConstructionWithEmptyJSONString() {
        try {
            JSONObject json = new JSONObject();
            json.put("key", "value");
            cut = EnhancedSecurityLabel.buildLabel(json);
            assertNull(cut);
        } catch (JSONException e) {
            fail();
        }
    }
    
    /**
     * Test to make sure that the equals operator has been implemented correctly.
     */
    @Test
    public void testEquality() {
        try {
            cut = new EnhancedSecurityLabel(PROTECTIVE_MARKING_FIXTURE);
            
            EnhancedSecurityLabel same = new EnhancedSecurityLabel(PROTECTIVE_MARKING_FIXTURE);
            
            EnhancedSecurityLabel different = new EnhancedSecurityLabel("SECRET");
            
            assertEquals(cut, same);
            
            assertFalse(cut.equals(different));
            assertFalse(cut.equals(null));
            assertFalse(cut.equals("SECRET"));
            
            
        } catch (Exception e) {
            fail();
        }
    }
    
    /**
     * testing equality with a two fully populated ESLs. 
     */
    @Test
    public void testExtendedEquality() {
        try {
            cut = new EnhancedSecurityLabel(PROTECTIVE_MARKING_FIXTURE);
            EnhancedSecurityLabel same = new EnhancedSecurityLabel(PROTECTIVE_MARKING_FIXTURE);
            
            cut.setNationalityOwner(NATIONALITY_OWNER_FIXTURE);
            same.setNationalityOwner(NATIONALITY_OWNER_FIXTURE);
            
            cut.setNationalityCaveats(NATIONALITY_CAVEATS_FIXTURE);
            same.setNationalityCaveats(NATIONALITY_CAVEATS_FIXTURE);
            
            cut.setCaveat(FREEFORM_TEXT_FIXTURE);
            same.setCaveat(FREEFORM_TEXT_FIXTURE);
            
            String[] openGroups = {"OG1", "OG2", "OG3", "OG4"};
            String[] closedGroups = {"CG1", "CG2", "CG3", "CG4"};
            String[] atomalMarkings = {"ATOMAL1", "ATOMAL2"};
            
            // Initialise the same list of open groups
            for (String open : openGroups) {
                cut.addOpenGroup(open);
                same.addOpenGroup(open);
            }
            
            // Initialise the same list of closed groups
            for (String closed : closedGroups) {
                cut.addClosedGroup(closed);
                same.addClosedGroup(closed);
            }
            
            // Initialise the same list of atomal markings
            for (String atomal : atomalMarkings) {
                cut.addAtomal(atomal);
                same.addAtomal(atomal);
            }
            
            assertEquals(cut, same);
            
        } catch (Exception e) {
            fail();
        }
    }
    
    /**
     * Test that a label can be correctly built from a set of properties extracted from the repository
     */
    @Test
    public void testLabelSuccessfullyConstructedFromProperties() {
        
        assertNotNull("Unable to construct ESL from repository properties", new EnhancedSecurityLabel(propertiesFixture));
        assertNotNull(new EnhancedSecurityLabel(propertiesFixture).toString());
    }
    
    @Test
    public void testIncompleteLabelSuccessfullyContrusctedFromProperties() {
        propertiesFixture.put(OPEN_GROUPS, null);
        
        assertNotNull(new EnhancedSecurityLabel(propertiesFixture).toString());
    }

}
