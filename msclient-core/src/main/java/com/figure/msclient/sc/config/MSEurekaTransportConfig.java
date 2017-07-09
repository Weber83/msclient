package com.figure.msclient.sc.config;

import com.netflix.discovery.shared.transport.EurekaTransportConfig;

/**
 * Created by chuanbo.wei on 2017/3/19.
 */
public class MSEurekaTransportConfig implements EurekaTransportConfig {

    private int sessionedClientReconnectIntervalSeconds = 20 * 60;

    private double retryableClientQuarantineRefreshPercentage = 0.66;

    private int bootstrapResolverRefreshIntervalSeconds = 5 * 60;

    private int applicationsResolverDataStalenessThresholdSeconds = 5 * 60;

    private int asyncResolverRefreshIntervalMs = 5 * 60 * 1000;

    private int asyncResolverWarmUpTimeoutMs = 5000;

    private int asyncExecutorThreadPoolSize = 5;

    private String readClusterVip;

    private String writeClusterVip;

    private boolean bootstrapResolverForQuery = true;

    private String bootstrapResolverStrategy;

    private boolean applicationsResolverUseIp = false;

    @Override
    public boolean useBootstrapResolverForQuery() {
        return this.bootstrapResolverForQuery;
    }

    @Override
    public boolean applicationsResolverUseIp() {
        return this.applicationsResolverUseIp;
    }

    @Override
    public int getSessionedClientReconnectIntervalSeconds() {
        return sessionedClientReconnectIntervalSeconds;
    }

    public void setSessionedClientReconnectIntervalSeconds(int sessionedClientReconnectIntervalSeconds) {
        this.sessionedClientReconnectIntervalSeconds = sessionedClientReconnectIntervalSeconds;
    }

    @Override
    public double getRetryableClientQuarantineRefreshPercentage() {
        return retryableClientQuarantineRefreshPercentage;
    }

    public void setRetryableClientQuarantineRefreshPercentage(double retryableClientQuarantineRefreshPercentage) {
        this.retryableClientQuarantineRefreshPercentage = retryableClientQuarantineRefreshPercentage;
    }

    public int getBootstrapResolverRefreshIntervalSeconds() {
        return bootstrapResolverRefreshIntervalSeconds;
    }

    public void setBootstrapResolverRefreshIntervalSeconds(int bootstrapResolverRefreshIntervalSeconds) {
        this.bootstrapResolverRefreshIntervalSeconds = bootstrapResolverRefreshIntervalSeconds;
    }

    @Override
    public int getApplicationsResolverDataStalenessThresholdSeconds() {
        return applicationsResolverDataStalenessThresholdSeconds;
    }

    public void setApplicationsResolverDataStalenessThresholdSeconds(int applicationsResolverDataStalenessThresholdSeconds) {
        this.applicationsResolverDataStalenessThresholdSeconds = applicationsResolverDataStalenessThresholdSeconds;
    }

    @Override
    public int getAsyncResolverRefreshIntervalMs() {
        return asyncResolverRefreshIntervalMs;
    }

    public void setAsyncResolverRefreshIntervalMs(int asyncResolverRefreshIntervalMs) {
        this.asyncResolverRefreshIntervalMs = asyncResolverRefreshIntervalMs;
    }

    @Override
    public int getAsyncResolverWarmUpTimeoutMs() {
        return asyncResolverWarmUpTimeoutMs;
    }

    public void setAsyncResolverWarmUpTimeoutMs(int asyncResolverWarmUpTimeoutMs) {
        this.asyncResolverWarmUpTimeoutMs = asyncResolverWarmUpTimeoutMs;
    }

    @Override
    public int getAsyncExecutorThreadPoolSize() {
        return asyncExecutorThreadPoolSize;
    }

    public void setAsyncExecutorThreadPoolSize(int asyncExecutorThreadPoolSize) {
        this.asyncExecutorThreadPoolSize = asyncExecutorThreadPoolSize;
    }

    @Override
    public String getReadClusterVip() {
        return readClusterVip;
    }

    public void setReadClusterVip(String readClusterVip) {
        this.readClusterVip = readClusterVip;
    }

    @Override
    public String getWriteClusterVip() {
        return writeClusterVip;
    }

    public void setWriteClusterVip(String writeClusterVip) {
        this.writeClusterVip = writeClusterVip;
    }

    @Override
    public String getBootstrapResolverStrategy() {
        return bootstrapResolverStrategy;
    }

    public void setBootstrapResolverStrategy(String bootstrapResolverStrategy) {
        this.bootstrapResolverStrategy = bootstrapResolverStrategy;
    }
}
