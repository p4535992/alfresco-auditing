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
package com.surevine.alfresco.audit.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.surevine.alfresco.audit.AlfrescoJSONKeys;
import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.esl.EnhancedSecurityLabel;

/**
 * This is the abstract event listener for http POST methods. It implements parsing of post data from the http request
 * object.
 * 
 * @author garethferrier
 * 
 */
public abstract class PostAuditEventListener extends AbstractAuditEventListener {
    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(PostAuditEventListener.class);


    public PostAuditEventListener(final String uriDesignator, final String action, final String method) {
        super(uriDesignator, action, method);
    }

    /**
     * Method that this listener implements - POST.
     */
    public static final String METHOD = "POST";

    /**
     * String that a valid JSON string starts with.
     */
    public static final String JSON_START_STRING = "{";

    /**
     * {@inheritDoc}
     * 
     * @throws JSONException
     */
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response) throws JSONException {

        Auditable toAudit = new AuditItem();
        JSONObject postContentJSONObject = parseJSONFromPostContent(request);
        if (postContentJSONObject != null) {
            setSecurityLabel(toAudit, postContentJSONObject);
            setTags(toAudit, postContentJSONObject);
        }
        
        setSpecificAuditMetadata(toAudit, request, postContentJSONObject, (BufferedHttpServletResponse) response);
        setGenericAuditMetadata(toAudit, request);

        List<Auditable> items = new ArrayList<Auditable>();
        items.add(toAudit);

        populateSecondaryAuditItems(items, request, response, postContentJSONObject);
        
        return items;
    }
    
    /**
     * Method which can be overridden my listeners which may wish to add extra audit events based on posted data
     * 
     * @param events the list of {@link Auditable} objects to which to add extra events
     * @param request
     * @param response
     * @param postContent the posted json
     * @throws JSONException 
     */
    protected void populateSecondaryAuditItems(final List<Auditable> events, final HttpServletRequest request,
            final HttpServletResponse response, final JSONObject postContent) throws JSONException {
        // Do nothing by default
    }

    /**
     * Assumes that the post content was a JSON string.
     * 
     * @param postContent
     *            from the request
     * @return valid JSONObject, otherwise null
     */
    public static JSONObject parseJSONFromPostContent(final HttpServletRequest request) {

    	if(request.getAttribute("com.surevine.alfresco.audit.JSONData") != null) {
    		return (JSONObject) request.getAttribute("com.surevine.alfresco.audit.JSONData");
    	}
    	
        JSONObject retVal = null;
        
        InputStream inStream;
        try {
            inStream = request.getInputStream();
        } catch (IOException eIO) {
            logger.error("Error encountered while reading from the request stream", eIO);
            return null;
        }
        
        InputStreamReader reader = new InputStreamReader(inStream);
        JSONTokener tokenizer = new JSONTokener(reader);
        
        try {
            retVal = new JSONObject(tokenizer);
        } catch (JSONException e) {
            // We will only warn in the logs if it was supposed to be JSON
            if("application/json".equals(request.getHeader("Content-Type"))) {
                try {
                    logger.warn("Invalid JSON string parsed from post content " + IOUtils.toString(request.getInputStream()), e);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }            
            }
        }
 
        request.setAttribute("com.surevine.alfresco.audit.JSONData", retVal);

        return retVal;
    }

    /**
     * Utility method to set the tags (if changed) on a auditable item.
     * 
     * @param toAudit
     *            the event
     * @param jsonObject
     *            containing the tags
     * @throws JSONException
     */
    protected void setTags(final Auditable toAudit, final JSONObject jsonObject) {
        if (jsonObject.has(AlfrescoJSONKeys.TAGS)) {
            Object obj;
            try {
                obj = jsonObject.get(AlfrescoJSONKeys.TAGS);
                if (obj instanceof JSONArray) {
                    if (jsonObject.has(AlfrescoJSONKeys.TAGS)) {
                        // Iterate over the tags and add to the auditable item.
                        JSONArray tags = jsonObject.getJSONArray(AlfrescoJSONKeys.TAGS);
                        StringBuilder buf = new StringBuilder();

                        for (int i = 0; i < tags.length(); i++) {
                            buf.append(tags.get(i).toString());

                            if (i != tags.length()) {
                                buf.append(", ");
                            }
                        }
                        toAudit.setTags(buf.toString());
                    }
                }
            } catch (JSONException e) {
                logger.warn("JSONException found parsing tags ", e);
            }

        }
    }

    /**
     * Utility method to set the security label on an audit item based on json.
     * 
     * @param toAudit
     * @param jsonObject
     */
    protected void setSecurityLabel(final Auditable toAudit, final JSONObject jsonObject) {
        EnhancedSecurityLabel esl;
        try {
            esl = EnhancedSecurityLabel.buildLabel(jsonObject);
            if (esl != null) {
                toAudit.setSecLabel(esl.toString());
            }
        } catch (JSONException e) {
            // There was no security label in the JSON - warn only.
            logger.warn("Unable to parse security label from JSON input");
        }
    }

    /**
     * @param auditable
     * @param request
     * @param json
     * @param responseJSONObject
     * @throws JSONException
     */
    public abstract void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) throws JSONException;

    /**
     * Default implementation. Can be used by some listeners.
     * 
     * @param request
     *            the servlet request
     * @param postContent
     *            post data if present
     * @return boolean whether an audit event is fired.
     */
    public boolean isEventFired(final HttpServletRequest request) {
        return request.getRequestURI().contains(getURIDesignator());
    }

}
