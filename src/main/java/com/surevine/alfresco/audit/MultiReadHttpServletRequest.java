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
