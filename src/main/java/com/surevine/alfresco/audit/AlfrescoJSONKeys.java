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
package com.surevine.alfresco.audit;

import org.json.JSONObject;

/**
 * @author garethferrier
 *
 */
public final class AlfrescoJSONKeys {

    /**
     * Hide the default constructor, as it should never be called.
     */
    private AlfrescoJSONKeys() {

    }

    /**
     * String literal for the page key.
     */
    public static final String PAGE = "page";

    /**
     * String little for accessing title in JSON object.
     */
    public static final String TITLE = "title";

    /**
     * String literal for accessing page title in JSON object.
     */
    public static final String PAGETITLE = "pageTitle";

    /**
     * Name of the key.
     */
    public static final String NAME = "name";

    /**
     * String literal for accessing permissions in the JSON object.
     */
    public static final String PERMISSIONS = "permissions";

    /**
     * String literal for accessing properties in the JSON object.
     */
    public static final String PROPERTIES = "properties";

    /**
     * String literal for accessing the filename in the JSON object.
     */
    public static final String FILENAME = "filename";
    /**
     * String literal for accessing single node reference in the JSON object.
     */
    public static final String NODEREF = "nodeRef";

    /**
     * String literal for accessing node references in the JSON object.
     */
    public static final String NODEREFS = "nodeRefs";

    /**
     * String literal for referencing itemTitle in a JSON object.
     */
    public static final String ITEM_TITLE = "itemTitle";

    /**
     * String literal for referencing site in a JSON object.
     */
    public static final String SITE = "site";

    /**
     * String literal for referencing discussion topic in a JSON object.
     */
    public static final String TOPIC = "topic";

    /**
     * String literal for referencing the container within a JSON object.
     */
    public static final String CONTAINER = "container";

    /**
     * String literal for referencing content in the JSON Object.
     */
    public static final String CONTENT = "content";

    /**
     * Current version, used for versioning documents, wiki pages, etc. 
     */
    public static final String CURRENT_VERSION = "currentVersion";

    /**
     * Any parameters provided with the page.
     */
    public static final String PAGE_PARAMS = "pageParams";

    /**
     * Content managed name.
     */
    public static final String CM_FOLDER_NAME = "prop_cm_name";

    /**
     * Content managed description.
     */
    public static final String CM_DESCRIPTION = "prop_cm_description";

    /**
     * Content Managed property.
     */
    public static final String CM_TITLE = "prop_cm_title";

    /**
     * Destination folder, used when uploading (and possibly moving) documents.
     */
    public static final String DEST_FOLDER = "alf_destination";

    /**
     * Looks to be a duplicate of pageContent.
     */
    // TODO Remove?
    public static final String WIKI_PAGE_CONTENT = "pageContent";

    /**
     * Minor version number.
     */
    public static final String VERSION = "version";

    /**
     * The major version number.
     */
    public static final String MAJOR_VERSION = "majorVersion";

    /**
     * Textual description.
     */
    public static final String DESCRIPTION = "description";

    /**
     * Context.
     */
    public static final String CONTEXT = "context";

    /**
     * For all tags.
     */
    public static final String TAGS = "tags";

    /**
     * Group based security.
     */
    public static final String GROUP = "group";

    /**
     * Role based security.
     */
    public static final String ROLE = "role";

    /**
     * The actual content of a page.
     */
    public static final String PAGE_CONTENT = "pageContent";

    /**
     * Code.
     */
    public static final String CODE = "code";

    /**
     * Status of a call.
     */
    public static final String STATUS = "status";

    /**
     * Whether or not a call has been successful.
     */
    public static final String OVERALL_SUCCESS = "overallSuccess";
    /**
     * Helper method for accessing a property from within the JSON object.
     *
     * @param property name of the property to check
     * @param json to find the property within
     * @return the value of the property if present, otherwise null.
     */
    public static String getPropertyValue(final String property, final JSONObject json) {
        try {
            if (json.has(property)) {
                return json.getString(property);
            }

        } catch (Exception e) {
            // Simply swallow the exception.
        }

        return null;
    }
}
