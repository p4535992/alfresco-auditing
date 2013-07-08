/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package com.surevine.alfresco.audit.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.surevine.alfresco.audit.AuditItem;
import com.surevine.alfresco.audit.Auditable;

/**
 * @author garethferrier
 * 
 */
public abstract class GetAuditEventListener extends AbstractAuditEventListener {

    /**
     * @param uriDesignator
     * @param action
     * @param method
     */
    public GetAuditEventListener(final String uriDesignator, final String action, final String method) {
        super(uriDesignator, action, method);
    }

    /**
     * 
     */
    public static final String METHOD = "GET";

    /**
     * {@inheritDoc}
     */
    public boolean isEventFired(final HttpServletRequest request, final String postContent) {
        return request.getRequestURI().contains(this.getURIDesignator());
    }

    /**
     * {@inheritDoc}
     * @throws JSONException 
     */
    public List<Auditable> populateAuditItems(final HttpServletRequest request, final HttpServletResponse response,
            final String postContent) throws JSONException {

        Auditable toAudit = new AuditItem();
        setGenericAuditMetadata(toAudit, request);
        setSpecificAuditMetadata(toAudit, request);

        List<Auditable> items = new ArrayList<Auditable>();
        items.add(toAudit);

        return items;
    }

    /**
     * @param audit
     * @param request
     * @throws JSONException 
     */
    public abstract void setSpecificAuditMetadata(final Auditable audit, final HttpServletRequest request) throws JSONException;
}
