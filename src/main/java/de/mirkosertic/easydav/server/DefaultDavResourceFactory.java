package de.mirkosertic.easydav.server;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.DavResource;
import org.apache.jackrabbit.webdav.DavResourceFactory;
import org.apache.jackrabbit.webdav.DavResourceLocator;
import org.apache.jackrabbit.webdav.DavServletRequest;
import org.apache.jackrabbit.webdav.DavServletResponse;
import org.apache.jackrabbit.webdav.DavSession;

import de.mirkosertic.easydav.fs.FSFile;
import de.mirkosertic.easydav.fs.VirtualFolder;

class DefaultDavResourceFactory implements DavResourceFactory {

    private final ConfigurationManager configurationManager;
    private final ResourceFactory resourceFactory;

    public DefaultDavResourceFactory(ConfigurationManager aConfigManager, ResourceFactory aResourceFactory) {
        resourceFactory = aResourceFactory;
        configurationManager = aConfigManager;
    }

    @Override
    public DavResource createResource(DavResourceLocator aLocator, DavServletRequest aRequest, DavServletResponse aResponse) throws DavException {

        Configuration theConfiguration = configurationManager.getConfigurationFor(aRequest);
        FSFile theRootFolder = theConfiguration.getRootFolder();

        if (aLocator.isRootLocation()) {
            return resourceFactory.createFolderResource(theRootFolder, aRequest.getDavSession(), this, aLocator);
        }

        String theResourcePath = aLocator.getResourcePath();
        if (theResourcePath.startsWith("/")) {
            theResourcePath = theResourcePath.substring(1);
        }
        FSFile theReference = theRootFolder.asChild(theResourcePath);
        if (theReference == null) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND, "Not found : "+theResourcePath);
        }

        return resourceFactory.createFileOrFolderResource(theReference, aRequest.getDavSession(), this, aLocator);
    }

    @Override
    public DavResource createResource(DavResourceLocator locator, DavSession session) throws DavException {
        throw new NotImplementedException("Not implemented");
    }
}
