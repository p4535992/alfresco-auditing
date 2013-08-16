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

import java.util.Date;

/**
 * @author garethferrier
 * 
 */
public interface Auditable {

    /**
     * @param user
     */
    void setUser(String user);

    /**
     * @return
     */
    String getUser();

    /**
     * @param url
     */
    void setUrl(String url);

    /**
     * @return
     */
    String getUrl();

    /**
     * @param version
     */
    void setVersion(String version);

    /**
     * @return
     */
    String getVersion();

    /**
     * @param details
     */
    void setDetails(String details);

    /**
     * @return
     */
    String getDetails();

    /**
     * @param action
     */
    void setAction(String action);

    /**
     * @return
     */
    String getAction();

    /**
     * @param success
     */
    void setSuccess(boolean success);

    /**
     * @return
     */
    boolean isSuccess();

    /**
     * @param secLabel
     */
    void setSecLabel(String secLabel);

    /**
     * @return
     */
    String getSecLabel();

    /**
     * @param source
     */
    void setSource(String source);

    /**
     * @return
     */
    String getSource();

    /**
     * @return remote address
     */
    String getRemoteAddress();

    /**
     * @param remoteAddr
     *            of client
     */
    void setRemoteAddress(String remoteAddr);

    /**
     * @param string
     *            site
     */
    void setSite(String string);

    /**
     * @return
     */
    String getSite();

    /**
     * @return date
     */
    Date getDate();

    /**
     * @param date
     */
    void setDate(Date date);

    /**
     * @return
     */
    String getTags();

    /**
     * @param tags
     */
    void setTags(String tags);

    String getNodeRef();
    
    void setNodeRef(String nodeRef);
    
    long getTimeSpent();
    
    void setTimeSpent(long timeSpent);

}
