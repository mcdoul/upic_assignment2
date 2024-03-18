package com.upic.server.model;

import java.util.List;

public class APIStats {
    private List<EndpointStat> endpointStats;

    public APIStats() {
    }

    public List<EndpointStat> getEndpointStats() {
        return endpointStats;
    }

    public void setEndpointStats(List<EndpointStat> endpointStats) {
        this.endpointStats = endpointStats;
    }

    public static class EndpointStat {
        private String URL;
        private String operation;
        private int mean;
        private int max;

        public EndpointStat() {
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public int getMean() {
            return mean;
        }

        public void setMean(int mean) {
            this.mean = mean;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }
}
