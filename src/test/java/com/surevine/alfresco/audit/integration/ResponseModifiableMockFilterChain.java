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

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.mock.web.MockFilterChain;

/**
 * This class is used so that we can inject our own implementation of the doFilter method such that we can write to the
 * response object when the decorating outputstream is attached.
 * 
 * @author garethferrier
 * 
 */
public class ResponseModifiableMockFilterChain extends MockFilterChain {
    private String content;
    private int status;

    /**
     * Constructor. Initialise the content and status.
     * 
     * @param content
     * @param status
     */
    public ResponseModifiableMockFilterChain(final String content, final int status) {
        this.content = content;
        this.status = status;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response) {
        super.doFilter(request, response);

        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.getOutputStream().write(content.getBytes());
            httpServletResponse.setContentLength(content.length());
            httpServletResponse.setStatus(this.status);
            httpServletResponse.flushBuffer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
