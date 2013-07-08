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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.audit.AuditException;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.site.SiteModel;
import org.alfresco.repo.transfer.PathHelper;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ISO9075;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author garethferrier
 * 
 */
public class NodeRefResolverImpl implements NodeRefResolver {

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * Logger for errors and warnings.
     */
    private static final Log logger = LogFactory.getLog(NodeRefResolverImpl.class);

    /**
     * String that is used to replace the removed string when formatting URI to resolve to a path.
     */
    private static final String STRING_TO_INSERT = "/sites";

    /**
     * String to remove from a URI so that it can be formatted to a path.
     */
    private static final String STRING_TO_REMOVE = "/site";
    /**
     * Alfresco core service.
     */
    private FileFolderService fileFolderService;
    /**
     * Alfresco core service.
     */
    private SearchService searchService;

    private Repository repository;

    private NodeService nodeService;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * Reference to company home - required as the start point for resolving paths.
     */
    private NodeRef companyHomeNodeRef;

    /**
     * initialise company home.
     */
    private void initialise() {
        ResultSet rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_XPATH,
                "/app:company_home");
        companyHomeNodeRef = null;

        if (rs.length() == 0) {
            throw new AlfrescoRuntimeException("Didn't find Company Home");
        }
        companyHomeNodeRef = rs.getNodeRef(0);

        rs.close();
    }

    /**
     * Spring accessible getter.
     * 
     * @return fileFolderService
     */
    public FileFolderService getFileFolderService() {
        return fileFolderService;
    }

    /**
     * Spring accessiable setter.
     * 
     * @param fileFolderService
     */
    public void setFileFolderService(final FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    /**
     * @return
     */
    public SearchService getSearchService() {
        return searchService;
    }

    /**
     * @param searchService
     */
    public void setSearchService(final SearchService searchService) {
        this.searchService = searchService;
    }

    public NodeRef getNodeRefFromPath(final String path) throws FileNotFoundException {
        return getNodeRefFromPath(path, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.surevine.alfresco.audit.NodeRefResolver#getNodeRefFromPath(java.lang.String)
     */
    public NodeRef getNodeRefFromPath(String path, boolean useFullPath) throws FileNotFoundException {
        if (companyHomeNodeRef == null) {
            initialise();
        }

        if (useFullPath) {
            path = path.replaceAll("\\{" + NamespaceService.APP_MODEL_1_0_URI + "\\}", NamespaceService.APP_MODEL_PREFIX + ":")
                    .replaceAll("\\{" + SiteModel.SITE_MODEL_URL + "\\}", SiteModel.SITE_MODEL_PREFIX + ":")
                    .replaceAll("\\{" + NamespaceService.CONTENT_MODEL_1_0_URI + "\\}", NamespaceService.CONTENT_MODEL_PREFIX + ":");
        }

        NodeRef retVal = null;

        FileInfo fi;
        try {
            List<String> serialisedPathString = serializePathString(path);
            if (logger.isDebugEnabled()) {
                logger.debug("Using the serialised path: " + serialisedPathString);
            }
            
            fi = fileFolderService.resolveNamePath(companyHomeNodeRef, serialisedPathString);

            retVal = fi.getNodeRef();

        } catch (UnsupportedEncodingException e) {
            logger.error("Unable to resolve nodeRef from " + path);
            return null;
        }
        return retVal;
    }

    /**
     * There are two types of Strings that may be input and processed similarly. Those strings that represent a path
     * that comes from the requested URI (i.e. doc library) and those that are namespace aware, as introduced during
     * Managed Deletion.
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public static List<String> serializePathString(final String path) throws UnsupportedEncodingException {

        final String companyHome = "/app:company_home";

        // First decide which type of string it is, if it begins with "/app:company_home" then it is the type of path
        // used for managed delete webscripts, otherwise assume it is the URI type used for doclib.
        String[] revisedPathArr;
        String revisedPath;

        if (path.startsWith(companyHome)) {
            // Now we need to dispense with company home and then loose each of the namespace qualifiers.
            revisedPath = path.substring(path.indexOf(companyHome) + companyHome.length(), path.length());
            Pattern p = Pattern.compile("/[a-z]+:");
            Matcher m = p.matcher(revisedPath);
            revisedPath = m.replaceAll("/");

            revisedPathArr = StringUtils.split(revisedPath, '/');

            // The last element will be ISO9075 encoded, and will need de-encoding
            if (revisedPathArr.length > 0) {
                revisedPathArr[revisedPathArr.length - 1] = ISO9075.decode(revisedPathArr[revisedPathArr.length - 1]);
            }
        } else {
            // Need to do a minor bit of fiddling assuming the path has come from a URI.
            revisedPath = STRING_TO_INSERT
                    + path.substring(path.lastIndexOf(STRING_TO_REMOVE) + STRING_TO_REMOVE.length());
            revisedPath = URLDecoder.decode(revisedPath, "UTF-8");

            revisedPathArr = StringUtils.split(revisedPath, '/');
        }

        return Arrays.asList(revisedPathArr);

    }

    @Override
    public NodeRef getNodeRefFromPath(Path path) throws AlfrescoRuntimeException {
        String query = "PATH:\"" + getShortPathString(path) + "\"";
        ResultSet rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_LUCENE,
                query);
        NodeRef nodeRef = null;
        try {
            if (rs.length() == 0) {
                logger.debug("Didn't find Node with Path: " + path.toString());
            }

            nodeRef = rs.getNodeRef(0);
        } finally {
            rs.close();
        }

        return nodeRef;
    }

    @Override
    public String getShortPathString(Path path) {
        String pathString;

        if (logger.isDebugEnabled()) {
            logger.debug("Called getShortPathString for path: " + path);
        }

        try {
            pathString = URLDecoder.decode(path.toString(), "UTF-8");
        } catch (UnsupportedEncodingException eUE) {
            throw new AlfrescoRuntimeException("UTF-8 isn't supported on this platform", eUE);
        }
        String lastElement, pathStringWithoutLastElement;
        if (pathString.lastIndexOf("/{") > -1) // Long form path
        {
            pathStringWithoutLastElement = pathString.substring(0, pathString.lastIndexOf("}") + 1);
            lastElement = pathString.substring(pathString.lastIndexOf("}") + 1);
        } else // Short form path, so just return input path - we should probably never have called this method anyway
        {
            if (logger.isDebugEnabled()) {
                logger.debug("Path already in short form");
            }
            return path.toString();
        }

        pathStringWithoutLastElement = pathStringWithoutLastElement
                .replaceAll("\\{" + NamespaceService.APP_MODEL_1_0_URI + "\\}", "app:")
                .replaceAll("\\{" + SiteModel.SITE_MODEL_URL + "\\}", "st:")
                .replaceAll("\\{" + NamespaceService.CONTENT_MODEL_1_0_URI + "\\}", "cm:")
                .concat(ISO9075.encode(lastElement));

        if (logger.isDebugEnabled()) {
            logger.debug("Path converted to short form: " + pathStringWithoutLastElement);
        }

        return pathStringWithoutLastElement;
    }

    private Path convertRequestURIToPath(final String requestURI) {

        final String SITE = SiteModel.TYPE_SITE.getLocalName() + "/";
        final String PAGE = "slingshot/wiki/page/";

        String pageName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        String token = null;
        if (requestURI.contains(PAGE)) {
            token = PAGE;
        } else {
            token = SITE;
        }

        int siteStringStartIndex = requestURI.indexOf(token) + token.length();
        int siteStringEndIndex = requestURI.indexOf("/", siteStringStartIndex);
        String siteString = requestURI.substring(siteStringStartIndex, siteStringEndIndex);
        
        if (siteString == null || siteString.length() == 0) {
            // For whatever reason a site was not parsed from the string
            return null;
        }
        
        
        String resourceType = null;

        if (requestURI.contains("forum/post")) {
            resourceType = "discussions";
        } else if (requestURI.contains("wiki")) {
            resourceType = "wiki";
        } else {
            resourceType = "doclib";
        }

        StringBuffer pathStringBuf = new StringBuffer();
        pathStringBuf.append("/").append(QName.createQName(NamespaceService.APP_MODEL_1_0_URI, "company_home"));
        pathStringBuf.append("/").append(SiteModel.TYPE_SITES);
        pathStringBuf.append("/").append(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, siteString));
        pathStringBuf.append("/").append(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, resourceType));
        pathStringBuf.append("/").append(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, pageName));

        return PathHelper.stringToPath(pathStringBuf.toString());
    }

    public NodeRef getNodeRef(final String request) {
        
        StoreRef store = null;
        String nodeIDString = null;
        // Deal with the scenario where there is a store ref.
        if (request.contains(spacesStoreTestString)) {
             store = StoreRef.STORE_REF_WORKSPACE_SPACESSTORE;
        } else if (request.contains(versionStoreTestString)) {
            store = STORE_REF_VERSION_SPACES_STORE;
        }
        
        if (store != null) {
            String testString = store.getProtocol() + "/" + store.getIdentifier() + "/";

            // The string is present, now just parse out the UUID
            int startIndex = request.indexOf(testString) + testString.length();
            nodeIDString = request.substring(startIndex, startIndex + GUID_LENGTH);            
        }
        
        // There is a further scenario - for versioned wiki page access
        String VERSIONED_WIKI_PAGE_ACCESS = "slingshot/wiki/version";
        if (request.contains(VERSIONED_WIKI_PAGE_ACCESS)) {
            store = STORE_REF_VERSION_SPACES_STORE;
            nodeIDString = request.substring(request.lastIndexOf("/") + 1, request.length());
        }
        
        if (store != null) {
            return findNodeRefInStore(store, nodeIDString);
        }

        NodeRef node = null;
        try {
            node = getNodeRefFromPath(getShortPathString(convertRequestURIToPath(request)));
        } catch (FileNotFoundException e) {
            throw new AuditException("Invalid node resolved attempting to audit wiki page with URI: " + request, e);
        }

        if (node != null && getNodeService().exists(node)) {
            return node;
        } else {
            logger.debug("Invalid node resolved attempting to audit wiki page with URI: " + request);
            return null;
        }

    }

    private NodeRef findNodeRefInStore(StoreRef location, String nodeIDString) {

        // Create an array with the {store_type}, {store_id} and {node_id}
        String[] reference = { location.getProtocol(), location.getIdentifier(), nodeIDString };

        return repository.findNodeRef("node", reference);
    }

    private static String spacesStoreTestString = StoreRef.STORE_REF_WORKSPACE_SPACESSTORE.getProtocol() + "/"
            + StoreRef.STORE_REF_WORKSPACE_SPACESSTORE.getIdentifier();

    public static final StoreRef STORE_REF_VERSION_SPACES_STORE = new StoreRef("versionStore", "version2Store");
    private static String versionStoreTestString = STORE_REF_VERSION_SPACES_STORE.getProtocol() + "/"
            + STORE_REF_VERSION_SPACES_STORE.getIdentifier();

    private static final int GUID_LENGTH = 36;

    /**
     * Extract the name of the site an item is in based on it's path within the repo
     * 
     * @param nodeRef
     *            NodeRef of an item
     * @return String indicating the name (without namespace) of the site the item is in
     */
    @Override
    public String getSiteName(NodeRef nodeRef) {
        
        final String SITES = "sites/";
        String pathStr = getNodeService().getPath(nodeRef).toString();
        int startIdx = pathStr.indexOf(SITES) + SITES.length(); // Start just after sites/
        startIdx = pathStr.indexOf("}", startIdx) + 1; // Then _really_ start after the next }, ie. the end of the
                                                       // namespace
        int endIdx = pathStr.indexOf("/", startIdx + 1); // Finish at the next / after the start
        return pathStr.substring(startIdx, endIdx);
    }

    @Override
    public NodeRef getNodeRefFromGUID(String nodeRefStr) throws AlfrescoRuntimeException {

        NodeRef potentialNode = null;

        if (nodeRefStr != null && NodeRef.isNodeRef(nodeRefStr)) {

            potentialNode = new NodeRef(nodeRefStr);
            if (!getNodeService().exists(potentialNode)) {
                logger.debug("Could not resolve node ref from string:" + nodeRefStr);
            }
        } else {
            logger.debug("Could not resolve node ref from string:" + nodeRefStr);
        }

        return potentialNode;
    }

}
