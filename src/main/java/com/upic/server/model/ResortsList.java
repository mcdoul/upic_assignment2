package com.upic.server.model;
import java.util.List;


public class ResortsList {
    private List<Resort> resorts;

    public ResortsList() {
    }

    public List<Resort> getResorts() {
        return resorts;
    }

    public void setResorts(List<Resort> resorts) {
        this.resorts = resorts;
    }

    public static class Resort {
        private String resortName;
        private int resortID;

        public Resort() {
        }

        public String getResortName() {
            return resortName;
        }

        public void setResortName(String resortName) {
            this.resortName = resortName;
        }

        public int getResortID() {
            return resortID;
        }

        public void setResortID(int resortID) {
            this.resortID = resortID;
        }
    }
}

