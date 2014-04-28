package de.mirkosertic.easydav.fs.local;

import de.mirkosertic.easydav.fs.FSFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileProxy implements FSFile {

    private String displayName;
    private File file;
    private FSFile parent;

    private FileProxy(File aFile) {
        this(aFile, aFile.getName());
    }

    public FileProxy(File aFile, String aDisplayName) {
        displayName = aDisplayName;
        file = aFile;
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public String getName() {
        return displayName;
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public void mkdirs() {
        file.mkdirs();
    }

    @Override
    public void delete() throws IOException {
        FileUtils.forceDelete(file);
    }

    @Override
    public OutputStream openWriteStream() throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public boolean renameTo(FSFile aNewFileName) {
        if (!(aNewFileName instanceof FileProxy)) {
            throw new NotImplementedException("Can only rename FileProxies to other FileProxies");
        }
        return file.renameTo(((FileProxy) aNewFileName).file);
    }

    @Override
    public FSFile parent() {
        return parent;
    }

    @Override
    public FileInputStream openInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    @Override
    public List<FSFile> listFiles() {
        List<FSFile> theFiles = new ArrayList<>();
        for (File theFile : file.listFiles()) {
            FileProxy theProxy = new FileProxy(theFile);
            theProxy.setParent(this);
            theFiles.add(theProxy);
        }
        return theFiles;
    }

    @Override
    public FSFile asChild(String aResourcePath) {
        FileProxy theProxy = new FileProxy(new File(file, aResourcePath));
        theProxy.setParent(this);
        return theProxy;
    }

    @Override
    public void setParent(FSFile aParent) {
        parent = aParent;
    }
}