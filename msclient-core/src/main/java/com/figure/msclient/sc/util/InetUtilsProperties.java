package com.figure.msclient.sc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/20.
 */
public class InetUtilsProperties {

    public static final String PREFIX = "spring.cloud.inetutils";

    /**
     * The default hostname. Used in case of errors.
     */
    private String defaultHostname = "localhost";

    /**
     * The default ipaddress. Used in case of errors.
     */
    private String defaultIpAddress = "127.0.0.1";

    /**
     * Timeout in seconds for calculating hostname.
     */
    private int timeoutSeconds = 1;

    /**
     * List of Java regex expressions for network interfaces that will be ignored.
     */
    private List<String> ignoredInterfaces = new ArrayList<String>();

    /**
     * Use only interfaces with site local addresses. See {@link java.net.InetAddress#isSiteLocalAddress()} for more details.
     */
    private boolean useOnlySiteLocalInterfaces = false;

    /**
     * List of Java regex expressions for network addresses that will be ignored.
     */
    private List<String> preferredNetworks = new ArrayList<String>();

    public String getDefaultHostname() {
        return defaultHostname;
    }

    public void setDefaultHostname(String defaultHostname) {
        this.defaultHostname = defaultHostname;
    }

    public String getDefaultIpAddress() {
        return defaultIpAddress;
    }

    public void setDefaultIpAddress(String defaultIpAddress) {
        this.defaultIpAddress = defaultIpAddress;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public List<String> getIgnoredInterfaces() {
        return ignoredInterfaces;
    }

    public void setIgnoredInterfaces(List<String> ignoredInterfaces) {
        this.ignoredInterfaces = ignoredInterfaces;
    }

    public boolean isUseOnlySiteLocalInterfaces() {
        return useOnlySiteLocalInterfaces;
    }

    public void setUseOnlySiteLocalInterfaces(boolean useOnlySiteLocalInterfaces) {
        this.useOnlySiteLocalInterfaces = useOnlySiteLocalInterfaces;
    }

    public List<String> getPreferredNetworks() {
        return preferredNetworks;
    }

    public void setPreferredNetworks(List<String> preferredNetworks) {
        this.preferredNetworks = preferredNetworks;
    }
}
