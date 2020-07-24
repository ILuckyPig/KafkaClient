package com.lu.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cluster {
    @JsonProperty("cluster_name")
    private String clusterName;
    @JsonProperty("bootstrap_server")
    private List<String> bootstrapServer;

    public Cluster() {

    }

    public Cluster(String clusterName, List<String> bootstrapServer) {
        this.clusterName = clusterName;
        this.bootstrapServer = bootstrapServer;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<String> getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(List<String> bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }
}
