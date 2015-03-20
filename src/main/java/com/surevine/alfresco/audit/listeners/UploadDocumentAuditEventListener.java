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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.alfresco.audit.Auditable;
import com.surevine.alfresco.audit.BufferedHttpServletResponse;
import com.surevine.alfresco.audit.BufferedServletOutputStream;

/**
 * @author garethferrier
 * 
 */
public class UploadDocumentAuditEventListener extends PostFormAuditEventListener {

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(UploadDocumentAuditEventListener.class);

    /**
     * Name persisted to identify event.
     */
    private static final String ACTION = "DOCUMENT_UPLOADED";

    /**
     * Part of URI used to identify the event.
     */
    private static final String URI_DESIGNATOR = "api/upload";
    
    public static final String MIME_LINE_DELIMITER = "\r\n";

    /**
     * Default constructor which provides statics to the super class.
     */
    public UploadDocumentAuditEventListener() {
        super(URI_DESIGNATOR, ACTION, METHOD);
    }

    @Override
    public boolean isEventFired(final HttpServletRequest request) {
        if(super.isEventFired(request)) {
            String formItemValue = formItemValues.get("overwrite");
            
            if((formItemValue != null) && formItemValue.equalsIgnoreCase("false")) {
                return true;
            }
        }
        
        return false;
    }

    private static final char JSON_START_CHAR = '{';
    private static final char JSON_END_CHAR = '}';
    private static final char HTML_START_CHAR = '<';

    @Override
    public void setSpecificAuditMetadata(final Auditable auditable, final HttpServletRequest request,
            final JSONObject json, final BufferedHttpServletResponse response) {

        super.setSpecificAuditMetadata(auditable, request, json, response);
        
        if (response == null) {
            throw new IllegalArgumentException("parameter was null");
        } else {
            // Now need to decide if the response coming back is JSON in the clear or wrapped up in HTML.
            try {
                byte[] responseBytes = ((BufferedServletOutputStream) response.getOutputStream())
                        .getOutputStreamAsByteArray();
                if (responseBytes.length > 0) {
                    byte firstByte = responseBytes[0];
                    String responseString = new String(responseBytes, "UTF-8");
                    
                    if (responseString.indexOf("<title>An error has occurred</title>\n") >= 0) {
                        logger.error("Auditing failed due to an unknown error.");
                        return;
                    }
                    
                    if (firstByte == HTML_START_CHAR) {
                        // Then it is most likely HTML, so trim it to be JSON only.
                        responseString = responseString.substring(responseString.indexOf(JSON_START_CHAR),
                                responseString.lastIndexOf(JSON_END_CHAR) + 1);
                    }

                    // Now the responseString should just contain the JSON, check then continue
                    if (responseString.startsWith(JSON_START_STRING)) {
                        // Then it is a JSON string or very likely, continue with parsing.
                        JSONObject responseJSON = new JSONObject(responseString);

                        if (responseJSON.has("fileName")) {
                            auditable.setSource(responseJSON.getString("fileName"));
                        }
                    }
                }
            } catch (JSONException e) {
                logger.warn("Unable to parse failure JSON definition");
            } catch (IOException io) {
                logger.warn("IOException caught parsing response", io);
            }

        }

    }

    /**
     * Parse a JSONObject from the response.
     * 
     * @param response
     *            object
     * @return JSONObject or null if not possible
     */
    @SuppressWarnings("unused")
    private JSONObject parseJSONFromResponse(final HttpServletResponse response) {
        JSONObject retVal = null;
        String responseMessage = null;

        if (response == null) {
            throw new IllegalArgumentException("parameter was null");
        } else {
            try {
                byte[] failure = ((BufferedServletOutputStream) response.getOutputStream())
                        .getOutputStreamAsByteArray();
                if (failure.length > 0) {
                    byte firstByte = failure[0];
                    if (firstByte == '{') {
                        // Then it is a JSON string or very likely, continue with parsing.
                        responseMessage = new String(failure, "UTF-8");
                        retVal = new JSONObject(responseMessage);
                    }
                }
            } catch (JSONException e) {
                logger.warn("Unable to parse failure JSON definition");
            } catch (IOException io) {
                logger.warn("IOException caught parsing response", io);
            }

        }

        return retVal;
    }
}
