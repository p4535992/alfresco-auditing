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
public class AuditItem implements Auditable {

    private Date timestamp;
    private String user;
    private String version;
    private String securityLabel;
    private boolean success;
    private String tags;
    private String search;
    private String site;
    private String action;
    private String page;
    private String details;
    private String url;
    private String source;
    private String remoteAddress;
    private String nodeRef;
    private long timeSpent;

    /**
     * Default constructor - initialise the security label string to a default
     */
    public AuditItem() {
        securityLabel = "unchanged";
        success = true;
    }
    /**
     * {@inheritDoc}
     */
    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    /**
     * {@inheritDoc}
     */
    public void setTimestamp(final Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }

    /**
     * {@inheritDoc}
     */
    public String getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public void setUser(final String user) {
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return version;
    }

    /**
     * {@inheritDoc}
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * {@inheritDoc}
     */
    public String getSecurityLabel() {
        return securityLabel;
    }

    /**
     * {@inheritDoc}
     */
    public void setSecurityLabel(final String securityLabel) {
        this.securityLabel = securityLabel;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * {@inheritDoc}
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**
     * {@inheritDoc}
     */
    public String getTags() {
        return tags;
    }

    /**
     * {@inheritDoc}
     */
    public void setTags(final String tags) {
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    public String getSearch() {
        return search;
    }

    /**
     * {@inheritDoc}
     */
    public void setSearch(final String search) {
        this.search = search;
    }

    /**
     * {@inheritDoc}
     */
    public String getSite() {
        return site;
    }

    /**
     * {@inheritDoc}
     */
    public void setSite(final String site) {
        this.site = site;
    }

    /**
     * {@inheritDoc}
     */
    public String getAction() {
        return action;
    }

    /**
     * {@inheritDoc}
     */
    public void setAction(final String action) {
        this.action = action;
    }

    /**
     * {@inheritDoc}
     */
    public void setPage(final String page) {
        this.page = page;
    }

    /**
     * {@inheritDoc}
     */
    public String getPage() {
        return page;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAction() + ", ");
        sb.append(getTimestamp() + ", ");
        sb.append(getVersion() + ", ");

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    /**
     * {@inheritDoc}
     */
    public String getDetails() {
        return details;
    }

    /**
     * {@inheritDoc}
     */
    public void setSecLabel(final String secLabel) {
        this.securityLabel = secLabel;
    }

    /**
     * {@inheritDoc}
     */
    public String getSecLabel() {
        return securityLabel;
    }

    /**
     * {@inheritDoc}
     */
    public String getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    public void setSource(final String source) {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    public String getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * {@inheritDoc}
     */
    public void setRemoteAddress(final String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Date getDate() {
        return new Date(timestamp.getTime());
    }

    public void setDate(Date date) {
        timestamp = new Date(date.getTime());
    }
    public String getNodeRef() {
        return nodeRef;
    }
    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }
    
    public long getTimeSpent() {
        return timeSpent;
    }
    
    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

}
