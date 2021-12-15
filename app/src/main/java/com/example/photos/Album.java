package com.example.photos;



import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    public String name;
    public ArrayList<Photo> lib;

    public Album(String name) {
        this.name=name;
        this.lib=new ArrayList<Photo>();
    }

    public void addPhoto(Photo temp) {
        lib.add(temp);
    }
    public void deletePhoto(Photo temp) {
        lib.remove(temp);
    }
    public ArrayList<Photo> getLib() {
        return lib;
    }
    public String toString() {
        return name;
    }
}

