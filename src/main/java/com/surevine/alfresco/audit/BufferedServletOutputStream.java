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
package com.surevine.alfresco.audit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

/**
 * @author garethferrier
 *
 */
public class BufferedServletOutputStream extends ServletOutputStream {

    /**
     * The usual stream.
     */
    private final ServletOutputStream underlyingStream;
    
    /**
     * The copy of the stream
     */
    private final ByteArrayOutputStream baosCopy;

    /**
     * @param httpServletResponse
     * @throws IOException
     */
    public BufferedServletOutputStream(ServletResponse httpServletResponse) throws IOException {

        this.underlyingStream = httpServletResponse.getOutputStream();
        baosCopy = new ByteArrayOutputStream();
    }

    /**
     * @return
     */
    public byte[] getOutputStreamAsByteArray() {
        return baosCopy.toByteArray();
    }

    @Override
    public void write(int val) throws IOException {
        if (underlyingStream != null) {
            underlyingStream.write(val);
            baosCopy.write(val);
        }
    }

    @Override
    public void write(final byte[] byteArray) throws IOException {
        if (underlyingStream == null) {
            return;
        }

        write(byteArray, 0, byteArray.length);
    }

    @Override
    public void write(final byte byteArray[], final int offset, final int length) throws IOException {
        if (underlyingStream == null) {
            return;
        }
        underlyingStream.write(byteArray, offset, length);
        baosCopy.write(byteArray, offset, length);
    }

    @Override
    public void close() throws IOException {
        // TODO Are there possible issues with the underlying stream being closed before a flush if the servlet is using
        // a writer?
        underlyingStream.close();
    }

    @Override
    public void flush() throws IOException {
        if (underlyingStream == null) {
            return;
        }

        underlyingStream.flush();
        baosCopy.flush();
    }
    
    
}
