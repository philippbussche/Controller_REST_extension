package com.appdynamics.ace.extension.rest.util.command;

/**
 * Created by stefan.marx on 18.12.13.
 */
public class ExtensionInfo {
    private String _version;
    private boolean _installed;

    public ExtensionInfo() {
        setVersion("1.0");
        setInstalled(true);
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
}
