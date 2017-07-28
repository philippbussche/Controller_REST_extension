package com.appdynamics.ace.extension.rest.util.command;


import com.appdynamics.ace.extension.rest.util.RestExtensionServletWrapper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by stefan.marx on 18.12.13.
 */
public class ExtensionInfo {
    public ArrayList<String> getLoadedClasses() {
        return _loadedClasses;
    }

    private final ArrayList<String> _loadedClasses;
    private String _version;
    private boolean _installed;
    private List<FileInfo> _fileList;

    public ExtensionInfo() {
        setVersion("1.4.3");
        setInstalled(true);
        List<File> jars = RestExtensionServletWrapper.getJars(RestExtensionServletWrapper.getRestExtensionsRootDir());

        List<FileInfo> fi = new ArrayList<FileInfo>();
        for (File j : jars) {
            fi.add(new FileInfo(j.getName(), j.length(), j.lastModified()));
        }

        setFileList(fi);

        _loadedClasses = new ArrayList<String>();

        Set<Class<?>> clazzes = RestExtensionServletWrapper.getInstance().getLoaddedClasses();
        for (Class cl : clazzes) {
            _loadedClasses.add(cl.getName());
        }


    }

    public void setVersion(String version) {
        _version = version;
    }

    public String getVersion() {
        return _version;
    }

    public void setInstalled(boolean installed) {
        _installed = installed;
    }

    public boolean isInstalled() {
        return _installed;
    }

    private List<File> getJars(File rootDir) {
        ArrayList<File> erg = new ArrayList<File>();
        Collections.addAll(erg, rootDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".jar");
            }
        }));


        return erg;

    }

    public void setFileList(List<FileInfo> fileList) {
        _fileList = fileList;
    }

    public List<FileInfo> getFileList() {
        return _fileList;
    }
}
