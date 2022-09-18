package com.ahmed.ahmed.model;

import java.io.Serializable;

public class Section implements Serializable {
    private int id;
    private String Name;

    public Section(int id, String name) {
        this.id = id;
        Name = name;
    }

    public Section() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
