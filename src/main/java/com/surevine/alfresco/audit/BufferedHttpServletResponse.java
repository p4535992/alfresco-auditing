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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This class allows for anything that is written to the servlet outputstream of output writer to be
 * captured and written to a byte array so that it can be interogated to understand its contents
 * and still have the original message sent to the client.  This will be used to assert whether or
 * not the response is considered successful.
 * 
 * @author garethferrier
 *
 */
public class BufferedHttpServletResponse extends HttpServletResponseWrapper {

    private BufferedServletOutputStream servletOutputStream;
    private PrintWriter printWriter;

    public BufferedHttpServletResponse(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
    }
    
    // Default to good news
    private int status = HttpServletResponse.SC_OK;

    @Override
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);
        this.status = sc;
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.status = sc;
    }

    public int getStatus() {
        return status;
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream == null) {
            servletOutputStream = new BufferedServletOutputStream(this.getResponse());
        }
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.printWriter == null) {
            this.printWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), "UTF-8"), true);
        }
        return this.printWriter;
    }

    @Override
    public void flushBuffer() {
        if (this.printWriter != null) {
            this.printWriter.flush();
        }
    }

    byte[] getOutputBuffer() {
        // servletOutputStream can be null if the getOutputStream method is never
        // called.
        if (servletOutputStream != null) {
            return servletOutputStream.getOutputStreamAsByteArray();
        } else {
            return null;
        }
    }

    void finish() throws IOException {
        if (this.printWriter != null) {
            this.printWriter.close();
        }
        if (this.servletOutputStream != null) {
            this.servletOutputStream.close();
        }
    }
}
