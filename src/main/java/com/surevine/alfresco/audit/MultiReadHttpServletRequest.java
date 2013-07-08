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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
	  private byte[] body;

	    public MultiReadHttpServletRequest(HttpServletRequest httpServletRequest) throws IOException {
	        super(httpServletRequest);
	        // Read the request body and save it as a byte array
	        InputStream is = super.getInputStream();
	        body = IOUtils.toByteArray(is);
	    }

		@Override
	    public ServletInputStream getInputStream() throws IOException {
	        return new ServletInputStreamImpl(new ByteArrayInputStream(body));
	    }
		
		@Override
	    public BufferedReader getReader() throws IOException {
	        String enc = getCharacterEncoding();
	        if (enc == null) {
                enc = "UTF-8";
            }
	        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
	    }


		/**
		 * @author garethferrier
		 *
		 */
		private static class ServletInputStreamImpl extends ServletInputStream {

	        private InputStream is;

	        public ServletInputStreamImpl(InputStream is) {
	            this.is = is;
	        }

	        public int read() throws IOException {
	            return is.read();
	        }

	        public boolean markSupported() {
	            return false;
	        }

	        public synchronized void mark(int i) {
	            throw new RuntimeException(new IOException("mark/reset not supported"));
	        }

	        public synchronized void reset() throws IOException {
	            throw new IOException("mark/reset not supported");
	        }
	    }
}
