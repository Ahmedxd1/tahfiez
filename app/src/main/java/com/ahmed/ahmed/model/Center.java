package com.ahmed.ahmed.model;

import java.io.Serializable;

public class Center implements Serializable {
    private String id;
    private String NameCenter;
    private String Section;
    private String image;
    private String NumberCycles;
    private String userManger;
    private Address Address;

    public Center() {
    }

    public Center(String id, String nameCenter, String section, String image, String numberCycles, String userManger, Address address) {
        this.id = id;
        NameCenter = nameCenter;
        Section = section;
        this.image = image;
        NumberCycles = numberCycles;
        this.userManger = userManger;
        Address = address;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCenter() {
        return NameCenter;
    }

    public void setNameCenter(String nameCenter) {
        NameCenter = nameCenter;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumberCycles() {
        return NumberCycles;
    }

    public void setNumberCycles(String numberCycles) {
        NumberCycles = numberCycles;
    }

    public String getUserManger() {
        return userManger;
    }

    public void setUserManger(String userManger) {
        this.userManger = userManger;
    }

    public com.ahmed.ahmed.model.Address getAddress() {
        return Address;
    }

    public void setAddress(com.ahmed.ahmed.model.Address address) {
        Address = address;
    }

}
