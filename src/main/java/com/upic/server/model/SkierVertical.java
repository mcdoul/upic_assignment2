package com.upic.server.model;

import java.util.List;

public class SkierVertical {
    private List<ResortSeasonTotal> resorts;

    public SkierVertical() {
    }

    public List<ResortSeasonTotal> getResorts() {
        return resorts;
    }

    public void setResorts(List<ResortSeasonTotal> resorts) {
        this.resorts = resorts;
    }

    public static class ResortSeasonTotal {
        private String seasonID;
        private int totalVert;

        public ResortSeasonTotal(String seasonId, int totalVert) {
            this.seasonID = seasonId;
            this.totalVert = totalVert;
        }

        public String getSeasonID() {
            return seasonID;
        }

        public void setSeasonID(String seasonID) {
            this.seasonID = seasonID;
        }

        public int getTotalVert() {
            return totalVert;
        }

        public void setTotalVert(int totalVert) {
            this.totalVert = totalVert;
        }
    }
}
