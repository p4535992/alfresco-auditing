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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class MultiReadHttpServletRequestTest {

    @Test
    public void testVerySmallMemoryCache() throws Exception {
        testRequestWithSize(10);
    }
    
    @Test
    public void testSmallMemoryCache() throws Exception {
        testRequestWithSize(1024);  // 1kB
    }

    @Test
    public void testMediumMemoryCache() throws Exception {
        testRequestWithSize(1024 * 1025 * 5);  // 5MB
    }

    @Test
    public void testLargeRequestDoesntUseTonsOfMemory() throws Exception {
        MultiReadHttpServletRequest result = testRequestWithSize(1024 * 1024 * 20);  // 20MB
    }

    MultiReadHttpServletRequest testRequestWithSize(int size) throws Exception {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        byte[] testData = getTestData(size);
        
        System.out.println(testData.length);
        
        mockRequest.setContent(testData);  // A request
        
        MultiReadHttpServletRequest request = new MultiReadHttpServletRequest(mockRequest);
        
        byte[] result1 = IOUtils.toByteArray(request.getInputStream());
        System.out.println(result1.length);

        assertTrue("result1 should equal the input array. [" + testData.length + ", " + result1.length + "]", Arrays.equals(testData, result1));

        result1 = null;
        System.gc();
        
        byte[] result2 = IOUtils.toByteArray(request.getInputStream());
        assertTrue("result2 should equal the output array [" + testData.length + ", " + result2.length + "]", Arrays.equals(testData, result2));
        
        return request;
    }

    byte[] getTestData(int size) {
        byte[] data = new byte[size];
        
        for(int i = 0; i < size; ++i) {
            data[i] = (byte) Math.floor(Math.random() * 256);
        }
        
        return data;
    }
}
