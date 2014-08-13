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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.alfresco.util.TempFileProvider;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
    private static final Logger logger = Logger.getLogger(MultiReadHttpServletRequest.class);

    private static final int CACHE_INITIAL_SIZE = 1024 * 1024; // Start with a 1MB cache
    private static final int CACHE_MAX_MEMORY_SIZE = 10 * 1024 * 1024; // We will cache up to 10MB before using disk
    private static final int CACHE_INCREASE_SIZE = 1024 * 1024; // We will increase the size by 1MB each time

    private byte[] cache;
    private long cacheBytes;
    private File cacheFile;

    public MultiReadHttpServletRequest(HttpServletRequest httpServletRequest) throws IOException {
        super(httpServletRequest);

        long cacheSize = CACHE_INITIAL_SIZE;
        cacheBytes = 0;

        // Note that we don't necessarily trust the Content-Length header
        if (httpServletRequest.getHeader("Content-Length") != null) {
            try {
                cacheSize = Long.parseLong(httpServletRequest.getHeader("Content-Length"));
            } catch (NumberFormatException e) {
                logger.warn(
                        "Invalid Content-Length header encountered:" + httpServletRequest.getHeader("Content-Length"),
                        e);
            }
        }

        InputStream is = super.getInputStream();

        while (cacheSize <= CACHE_MAX_MEMORY_SIZE) {
            // Start reading the stream to memory
            byte[] newCache = new byte[(int) cacheSize];

            if (cache != null) {
                System.arraycopy(cache, 0, newCache, 0, cache.length);
            }

            cache = newCache;

            int bytesRead = is.read(cache, (int) cacheBytes, (int) (cache.length - cacheBytes));

            if(bytesRead == -1) {
                return;
            }
            
            //try testing is.available
            
            cacheBytes += bytesRead;

            if (cacheBytes == cache.length) {
                // We have filled the cache, let's increase the size
                cacheSize += CACHE_INCREASE_SIZE;
            } 
        }

        // If we've got here we've either hit the cache limit, or the Content-Length was too big to start with
        cacheFile = TempFileProvider.createTempFile("audit", "");
        
        FileOutputStream fos = new FileOutputStream(cacheFile);
        
        // If we have already cached some stuff, write it out to the file
        if(cacheBytes > 0) {
            fos.write(cache, 0, (int) cacheBytes);
            cache = null;
        }
        
        // Then copy the rest of the stream to the file
        IOUtils.copy(is, fos);
        
        fos.close();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cacheFile == null) {
            return new ServletInputStreamImpl(new ByteArrayInputStream(cache, 0, (int) cacheBytes));
        } else {
            return new ServletInputStreamImpl(new FileInputStream(cacheFile));
        }
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
