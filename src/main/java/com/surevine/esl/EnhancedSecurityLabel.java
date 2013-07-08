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
package com.surevine.esl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.SpringAuditFilterBean;

/**
 * This is a POJO implementation of the EnhancedSecurityLabel. It was originally written to support the auditing of user
 * actions, i.e. application of a label, increasing sensitivity of a label etc, but should be sufficiently generic that
 * it can be used or adapted before use to any arena where an EnhancedSecurityLabel may be required.
 * 
 * @author garethferrier
 * 
 */
public class EnhancedSecurityLabel {

    private static final Log logger = LogFactory.getLog(SpringAuditFilterBean.class);

    public static final String ATOMAL_JSON_STR = "eslAtomal";
    public static final String ATOMAL_MIME_STR = ATOMAL_JSON_STR.toLowerCase();
    public static final String CAVEATS_JSON_STR = "eslCaveats";
    public static final String CAVEATS_MIME_STR = CAVEATS_JSON_STR.toLowerCase();
    public static final String CLOSED_GROUPS_JSON_STR = "eslClosedGroupsHidden";
    public static final String CLOSED_GROUPS_MIME_STR = CLOSED_GROUPS_JSON_STR.toLowerCase();
    public static final String ORGANISATIONS_JSON_STR = "eslOrganisationsHidden";
    public static final String ORGANISATIONS_MIME_STR = ORGANISATIONS_JSON_STR.toLowerCase();
    public static final String OPEN_GROUPS_JSON_STR = "eslOpenGroupsHidden";
    public static final String OPEN_GROUPS_MIME_STR = OPEN_GROUPS_JSON_STR.toLowerCase();
    public static final String PROTECTIVE_MARKING_JSON_STR = "eslProtectiveMarking";
    public static final String PROTECTIVE_MARKING_MIME_STR = PROTECTIVE_MARKING_JSON_STR.toLowerCase();
    public static final String NATIONALITY_OWNER_JSON_STR = "eslNationalOwner";
    public static final String NATIONALITY_OWNER_MIME_STR = NATIONALITY_OWNER_JSON_STR.toLowerCase();
    public static final String NATIONAL_CAVEATS_JSON_STR = "eslNationalCaveats";
    public static final String NATIONAL_CAVEATS_MIME_STR = NATIONAL_CAVEATS_JSON_STR;
    public static final String DELIMITER = ",";
    public static final String ESL_EYES = "eslEyes";

    public static final String ESL_MODEL_0_3_URI = "http://www.alfresco.org/model/enhancedSecurity/0.3";

    public static final QName NATIONALITY_OWNER = QName.createQName(ESL_MODEL_0_3_URI, "nod");
    public static final QName PROTECTIVE_MARKING = QName.createQName(ESL_MODEL_0_3_URI, "pm");
    public static final QName OPEN_GROUPS = QName.createQName(ESL_MODEL_0_3_URI, "openMarkings");
    public static final QName CLOSED_GROUPS = QName.createQName(ESL_MODEL_0_3_URI, "closedMarkings");
    public static final QName FREE_FORM_CAVEATS = QName.createQName(ESL_MODEL_0_3_URI, "freeFormCaveats");
    public static final QName NATIONALITY_CAVEATS = QName.createQName(ESL_MODEL_0_3_URI, "nationalityCaveats");
    public static final QName ORGANISATIONS = QName.createQName(ESL_MODEL_0_3_URI, "organisations");

    public static final String JSON_ARRAY_DELIMITER = ",";

    private List<String> closedGroups = new ArrayList<String>();

    private List<String> openGroups = new ArrayList<String>();
    
    private List<String> organisations = new ArrayList<String>();

    private List<String> atomal = new ArrayList<String>();

    private String nationalityOwner;

    private String protectiveMarking;

    private String caveat;

    private String nationalityCaveats;

    /**
     * Empty default constructor.
     */
    public EnhancedSecurityLabel() {
        // Empty constructor
    }

    /**
     * Constructor used when creating an ESL when each of the expected mandatory field is provided.
     * 
     * @param nationalityOwner new nationlity owner
     * @param protectiveMarking new protective marking
     * @param nationalCaveats new nationality caveats
     */
    public EnhancedSecurityLabel(final String protectiveMarking) {
        this.protectiveMarking = protectiveMarking;
        if (this.protectiveMarking == null || "".equals(this.protectiveMarking)) {
            throw new IllegalArgumentException("Null or empty string provided when constructing ESL");
        }
    }

    /**
     * Constructor to create an ESL from the properties in the repository.
     * @param properties
     */
    @SuppressWarnings("unchecked")
    public EnhancedSecurityLabel(Map<QName, Serializable> properties) {
        
        // First off ensure there is a protective marking
        if (properties.containsKey(PROTECTIVE_MARKING)) {
            this.protectiveMarking = (String) properties.get(PROTECTIVE_MARKING);
            
            if (this.protectiveMarking == null || "".equals(this.protectiveMarking)) {
                throw new IllegalArgumentException("Null or empty string provided when constructing ESL");                
            }
        }
        
        // Now iterate over each of the other properties setting as necessary
        if (properties.containsKey(NATIONALITY_OWNER)) {
            this.nationalityOwner = (String) properties.get(NATIONALITY_OWNER);
        }

        if (properties.containsKey(FREE_FORM_CAVEATS)) {
            this.caveat = (String) properties.get(FREE_FORM_CAVEATS);
        }
        
        if (properties.containsKey(NATIONALITY_CAVEATS)) {
            this.nationalityCaveats = (String) properties.get(NATIONALITY_CAVEATS);
        }
        
        List<String> tmpContainer = null;
        if (properties.containsKey(OPEN_GROUPS)) {
            tmpContainer = (List<String>) properties.get(OPEN_GROUPS);
            if (tmpContainer != null) {
                this.openGroups = tmpContainer;
            }
        }
        
        tmpContainer = null;
        if (properties.containsKey(CLOSED_GROUPS)) {
            tmpContainer = (List<String>) properties.get(CLOSED_GROUPS);
            if (tmpContainer != null) {
                this.closedGroups = tmpContainer;
            }
        }

        tmpContainer = null;
        if (properties.containsKey(ORGANISATIONS)) {
            tmpContainer = (List<String>) properties.get(ORGANISATIONS);
            if (tmpContainer != null) {
                this.organisations = tmpContainer;
            }
        }
    }

    /**
     * Getter for the nationality owner
     * 
     * @return
     */
    public String getNationalityOwner() {
        return nationalityOwner;
    }
    
    /**
     * @param nationalityOwner
     */
    public void setNationalityOwner(final String nationalityOwner) {
        this.nationalityOwner = nationalityOwner;
    }

    /**
     * @return
     */
    public String getProtectiveMarking() {
        return protectiveMarking;
    }

    /**
     * @param protectiveMarking
     */
    public void setProtectiveMarking(final String protectiveMarking) {
        this.protectiveMarking = protectiveMarking;
    }

    /**
     * @return
     */
    public String getCaveat() {
        return caveat;
    }

    /**
     * @param caveat
     */
    public void setCaveat(final String caveat) {
        this.caveat = caveat;
    }

    /**
     * @return
     */
    public String getNationalityCaveats() {
        return nationalityCaveats;
    }

    /**
     * @param nationalityCaveats
     */
    /**
     * @param nationalityCaveats
     */
    public void setNationalityCaveats(String nationalityCaveats) {
        this.nationalityCaveats = nationalityCaveats;
    }

    /**
     * @param newAtomal
     */
    public void addAtomal(final String newAtomal) {
        atomal.add(newAtomal);
    }

    /**
     * @param newGroup
     */
    public void addOpenGroup(final String newGroup) {
        openGroups.add(newGroup);
    }
    
    public void addOrganisation(final String organisation) {
        organisations.add(organisation);
    }

    /**
     * @param newGroup
     */
    public void addClosedGroup(final String newGroup) {
        closedGroups.add(newGroup);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object compare) {
        // Self comparison.
        if (this == compare) {
            return true;
        }

        // Type comparison
        // Use instance of for 2 reasons
        // 1. Allows support for supertypes.
        // 2. Renders explicit check for null unnecessary
        if (!(compare instanceof EnhancedSecurityLabel)) {
            return false;
        }

        // Now the cast is safe
        EnhancedSecurityLabel compareESL = (EnhancedSecurityLabel) compare;
        return new EqualsBuilder().append(this.nationalityOwner, compareESL.nationalityOwner).append(
                this.nationalityCaveats, compareESL.nationalityCaveats).append(this.protectiveMarking,
                compareESL.protectiveMarking).append(this.caveat, compareESL.caveat).append(this.atomal,
                compareESL.atomal).append(this.openGroups, compareESL.openGroups).append(this.closedGroups,
                compareESL.closedGroups).append(this.organisations, compareESL.organisations).isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(1, 31).append(this.nationalityOwner).append(this.nationalityCaveats).append(
                this.protectiveMarking).append(this.caveat).append(this.atomal).append(this.openGroups).append(
                this.closedGroups).append(this.organisations).toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nationalityOwner + ", ");
        sb.append(protectiveMarking + ", ");
        if (!atomal.isEmpty()) {
            sb.append(atomal + ", ");
        }

        if (!openGroups.isEmpty()) {
            sb.append(openGroups + ", ");
        }

        if (!closedGroups.isEmpty()) {
            sb.append(closedGroups + ", ");
        }
        
        if (!organisations.isEmpty()) {
            sb.append(organisations + ", ");
        }

        if (caveat != null) {
            sb.append(caveat + ", ");
        }

        sb.append(nationalityCaveats);

        return sb.toString();
    }

    /**
     * @param set
     * @return
     */
    public static String getStringSet(final List<String> set) {
        StringBuilder sb = new StringBuilder();

        for (Iterator<String> iter = set.iterator(); iter.hasNext();) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    /**
     * @param jsonObject
     * @throws JSONException
     */
    public void addToJSON(final JSONObject jsonObject) throws JSONException {

        jsonObject.put(NATIONALITY_OWNER_JSON_STR, nationalityOwner);
        jsonObject.put(PROTECTIVE_MARKING_JSON_STR, protectiveMarking);
        if (!atomal.isEmpty()) {
            jsonObject.put(ATOMAL_JSON_STR, getStringSet(atomal));
        }

        if (!openGroups.isEmpty()) {
            jsonObject.put(OPEN_GROUPS_JSON_STR, getStringSet(openGroups));
        }

        if (!closedGroups.isEmpty()) {
            jsonObject.put(CLOSED_GROUPS_JSON_STR, getStringSet(closedGroups));
        }
        
        if (!organisations.isEmpty()) {
            jsonObject.put(ORGANISATIONS_JSON_STR, getStringSet(organisations));
        }

        jsonObject.put(CAVEATS_JSON_STR, caveat);
        jsonObject.put(NATIONAL_CAVEATS_JSON_STR, nationalityCaveats);
    }

    /**
     * Factory method for constructing an ESL from a JSON object.
     * 
     * @param json
     *            the object to build from
     * @return null if insufficient data present to create an ESL.
     * @throws JSONException
     *             if unexpected content in the JSON
     */
    public static EnhancedSecurityLabel buildLabel(final JSONObject json) throws JSONException {

        if (json == null) {
            return null;
        }

        String protectiveMarking = null;
        if (json.has(PROTECTIVE_MARKING_JSON_STR)) {
            protectiveMarking = json.getString(PROTECTIVE_MARKING_JSON_STR);
        } else {
            // There was no protective marking in the JSON so bail now.
            return null;
        }

        EnhancedSecurityLabel esl;
        try {
            esl = new EnhancedSecurityLabel(protectiveMarking);
        } catch (IllegalArgumentException iae) {
            logger.warn("Unable to construct EnhancedSecurityLabel", iae);
            return null;
        }
        
        String nationalityOwner = null;
        if (json.has(NATIONALITY_OWNER_JSON_STR)) {
            nationalityOwner = json.getString(NATIONALITY_OWNER_JSON_STR);
        }

        esl.setNationalityOwner(nationalityOwner);

        String nationalCaveats = null;
        if (json.has(NATIONAL_CAVEATS_JSON_STR)) {
            nationalCaveats = json.getString(NATIONAL_CAVEATS_JSON_STR);
        }

        esl.setNationalityCaveats(nationalCaveats);

        esl.setAtomal(parseListFromJSONString(json, ATOMAL_JSON_STR));
        esl.setOpenGroups(parseListFromJSONString(json, OPEN_GROUPS_JSON_STR));
        esl.setClosedGroups(parseListFromJSONString(json, CLOSED_GROUPS_JSON_STR));
        esl.setOrganisations(parseListFromJSONString(json, ORGANISATIONS_JSON_STR));

        
        if (json.has(CAVEATS_JSON_STR)) {
            esl.setCaveat(json.getString(CAVEATS_JSON_STR));
        }

        return esl;
    }
    
    

    /**
     * @param closedGroups new set of closed groups
     */
    public void setClosedGroups(final List<String> closedGroups) {
        if (closedGroups != null) {
            this.closedGroups = closedGroups;
        }
    }
    
    public void setOrganisations(final List<String> organisations) {
        if (organisations != null) {
            this.organisations = organisations;
        }
    }

    /**
     * @param openGroups new set of open groups
     */
    public void setOpenGroups(final List<String> openGroups) {
        if (openGroups != null) {
            this.openGroups = openGroups;
        }

    }

    /**
     * @param atomal new list of atomal markings.
     */
    public void setAtomal(final List<String> atomal) {
        if (atomal != null) {
            this.atomal = atomal;
        }
    }

    /**
     * Parse from a JSONObject a list of the items found based on the key.
     * 
     * @param json
     *            the object to retrieve from
     * @param jsonKey
     *            the key to access
     * @return a list of the values
     */
    public static List<String> parseListFromJSONString(final JSONObject json, final String jsonKey) {
        String str;
        if (json.has(jsonKey)) {
            try {
                str = json.getString(jsonKey);
                if (str != null && !str.isEmpty()) {
                    String[] split = str.split(JSON_ARRAY_DELIMITER);
                    return Arrays.asList(split);
                }
            } catch (JSONException e) {
                logger.warn("Error accessing list " + jsonKey, e);
            }

        }

        return null;
    }

}
