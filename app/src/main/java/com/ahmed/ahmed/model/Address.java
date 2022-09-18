package com.ahmed.ahmed.model;

import java.io.Serializable;

public class Address implements Serializable {
    String city;
    String address;
    double lat;
    double lng;


    public Address(String city, String address, double lat, double lng) {
        this.city = city;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public Address() {
    }

    public Address(String city, String address) {
        this.city = city;
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
