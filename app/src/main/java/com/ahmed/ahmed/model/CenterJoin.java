package com.ahmed.ahmed.model;

import java.io.Serializable;

public class CenterJoin implements Serializable {
    private int centerId;
    private int maximumCycles;
    private String centerName;
    private String centerImage;
    private String centerSection;
    private String centerAddress;
    private double centerLat;
    private double centerLong;

    public CenterJoin(int centerId, int maximumCycles, String centerName, String centerImage, String centerSection, String centerAddress) {
        this.centerId = centerId;
        this.maximumCycles = maximumCycles;
        this.centerName = centerName;
        this.centerImage = centerImage;
        this.centerSection = centerSection;
        this.centerAddress = centerAddress;
    }

    public CenterJoin(int centerId, int maximumCycles, String centerName, String centerImage, String centerSection, String centerAddress, double centerLat, double centerLong) {
        this.centerId = centerId;
        this.maximumCycles = maximumCycles;
        this.centerName = centerName;
        this.centerImage = centerImage;
        this.centerSection = centerSection;
        this.centerAddress = centerAddress;
        this.centerLat = centerLat;
        this.centerLong = centerLong;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public int getMaximumCycles() {
        return maximumCycles;
    }

    public void setMaximumCycles(int maximumCycles) {
        this.maximumCycles = maximumCycles;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterImage() {
        return centerImage;
    }

    public void setCenterImage(String centerImage) {
        this.centerImage = centerImage;
    }

    public String getCenterSection() {
        return centerSection;
    }

    public void setCenterSection(String centerSection) {
        this.centerSection = centerSection;
    }

    public String getCenterAddress() {
        return centerAddress;
    }

    public void setCenterAddress(String centerAddress) {
        this.centerAddress = centerAddress;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public double getCenterLong() {
        return centerLong;
    }

    public void setCenterLong(double centerLong) {
        this.centerLong = centerLong;
    }
}
