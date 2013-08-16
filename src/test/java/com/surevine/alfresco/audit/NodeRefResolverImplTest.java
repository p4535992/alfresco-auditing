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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.repository.Path.Element;
import org.junit.Test;


public class NodeRefResolverImplTest {

    
        private static final String PATH_FROM_URI = "/alfresco/s/slingshot/doclib/action/file/site/mytestsite/documentLibrary/MyDocuments";
        private static final String[] PATH_FROM_URI_STRINGS = {"sites", "mytestsite", "documentLibrary", "MyDocuments"};
    
        private static final String PATH_FROM_URI_UNENCODED_SQL = "/alfresco/s/slingshot/doclib/action/file/site/mytestsite/documentLibrary/My#Documents";
        private static final String[] PATH_FROM_URI_STRINGS_UNENCODED_SQL = {"sites", "mytestsite", "documentLibrary", "My#Documents"};

        
        private static final String NAMESPACED_PATH = "/app:company_home/st:sites/cm:sandbox/cm:wiki/cm:testing_audit";
        private static final String[] NAMESPACED_PATH_STRINGS = {"sites", "sandbox", "wiki", "testing_audit"};

        private static final String NAMESPACED_PATH_WITH_ENCODING = "/app:company_home/st:sites/cm:sandbox/cm:wiki/cm:_x0023_TestPage";
        private static final String[] NAMESPACED_PATH_WITH_ENCODING_STRINGS = {"sites", "sandbox", "wiki", "#TestPage"};

        private static final String LONG_PATH="{http://www.alfresco.org/model/application/1.0}company_home/{http://www.alfresco.org/model/site/1.0}sites/{http://www.alfresco.org/model/content/1.0}sandbox/{http://www.alfresco.org/model/content/1.0}wiki/{http://www.alfresco.org/model/content/1.0}1TestPage";
        private static final String[] LONG_PATH_PARTS={"{http://www.alfresco.org/model/application/1.0}company_home","{http://www.alfresco.org/model/site/1.0}sites","{http://www.alfresco.org/model/content/1.0}sandbox","{http://www.alfresco.org/model/content/1.0}wiki","{http://www.alfresco.org/model/content/1.0}1TestPage"};
        private static final String SHORT_VERSION_OF_LONG_PATH="app:company_home/st:sites/cm:sandbox/cm:wiki/cm:_x0031_TestPage";
        
        @Test
        public void testLongPath() throws FileNotFoundException {
                        
            NodeRefResolverImpl impl = new NodeRefResolverImpl();
            
            Path path = new Path();
            for (int i=0; i< LONG_PATH_PARTS.length; i++)
            {
                path.append(new PathElement(LONG_PATH_PARTS[i]));
            }
            assertEquals(LONG_PATH, path.toString());
            assertEquals(SHORT_VERSION_OF_LONG_PATH, impl.getShortPathString(path));
        }
        
        /**
         * Inner class implementation of Element to simplify the creation of Paths
         * @author simonw
         *
         */
        public static class PathElement extends Element
        {
            /**
             * 
             */
            private static final long serialVersionUID = -5935320087574557627L;
            private String _elementString;
            
            public PathElement(String elementString)
            {
                _elementString=elementString;
            }

            @Override
            public String getElementString() {
                return _elementString;
            }
        }
        
        @Test
        public void testSerializePathFromURI() {
            
            List<String> response;
            try {
                response = NodeRefResolverImpl.serializePathString(PATH_FROM_URI);
                
                for (int i = 0; i < PATH_FROM_URI_STRINGS.length; i++) {
                    assertEquals(PATH_FROM_URI_STRINGS[i], response.get(i));
                }
            } catch (UnsupportedEncodingException e) {
                fail();
            } 

        }
        
        @Test
        public void testSerializePathFromURIWithSQLEncoding() {
            
            List<String> response;
            try {
                response = NodeRefResolverImpl.serializePathString(PATH_FROM_URI_UNENCODED_SQL);
                
                for (int i = 0; i < PATH_FROM_URI_STRINGS_UNENCODED_SQL.length; i++) {
                    assertEquals(PATH_FROM_URI_STRINGS_UNENCODED_SQL[i], response.get(i));
                }
            } catch (UnsupportedEncodingException e) {
                fail();
            } 

        }
        
        @Test
        public void testSerializeNamespacedPath() {
            
            List<String> response;
            try {
                response = NodeRefResolverImpl.serializePathString(NAMESPACED_PATH);
                
                for (int i = 0; i < NAMESPACED_PATH_STRINGS.length; i++) {
                    assertEquals(NAMESPACED_PATH_STRINGS[i], response.get(i));
                }    
            } catch (UnsupportedEncodingException e) {
                fail();
            } 

        }
        
        @Test
        public void testSerializeNamespacedPathWithEncoding() {
            
            List<String> response;
            try {
                response = NodeRefResolverImpl.serializePathString(NAMESPACED_PATH_WITH_ENCODING);
                
                for (int i = 0; i < NAMESPACED_PATH_WITH_ENCODING_STRINGS.length; i++) {
                    assertEquals(NAMESPACED_PATH_WITH_ENCODING_STRINGS[i], response.get(i));
                }    
            } catch (UnsupportedEncodingException e) {
                fail();
            } 

        }
}
