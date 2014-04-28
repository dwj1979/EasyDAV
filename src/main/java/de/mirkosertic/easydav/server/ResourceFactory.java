package de.mirkosertic.easydav.server;

import de.mirkosertic.easydav.fs.FSFile;
import org.apache.jackrabbit.webdav.DavResource;
import org.apache.jackrabbit.webdav.DavResourceFactory;
import org.apache.jackrabbit.webdav.DavResourceLocator;
import org.apache.jackrabbit.webdav.DavSession;
import org.apache.jackrabbit.webdav.lock.LockManager;
import org.apache.jackrabbit.webdav.lock.SimpleLockManager;

public class ResourceFactory {

    private LockManager lockManager;

    public ResourceFactory() {
        lockManager = new SimpleLockManager();
    }

    FileDavResource createFileResource(FSFile aFile, DavSession aSession, DavResourceFactory aResourceFactory, DavResourceLocator aResourceLocator) {
        FileDavResource theResource = new FileDavResource(this, aFile, aSession, aResourceFactory, aResourceLocator);
        theResource.addLockManager(lockManager);
        theResource.initProperties();
        return theResource;
    }

    public FolderDavResource createFolderResource(FSFile aFile, DavSession aSession, DavResourceFactory aResourceFactory, DavResourceLocator aResourceLocator) {
        FolderDavResource theResource = new FolderDavResource(this, aFile, aSession, aResourceFactory, aResourceLocator);
        theResource.addLockManager(lockManager);
        theResource.initProperties();
        return theResource;
    }

    public DavResource createFileOrFolderResource(FSFile aFile, DavSession aSession, DavResourceFactory aResourceFactory, DavResourceLocator aResourceLocator) {
        if (aFile.isDirectory()) {
            return createFolderResource(aFile, aSession, aResourceFactory, aResourceLocator);
        }
        return createFileResource(aFile, aSession, aResourceFactory, aResourceLocator);
    }
}
