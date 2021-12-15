package com.example.photos;


import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


public class Photo implements Serializable {

    private String location;
    private ArrayList<Tag> lib;
    private String checkPath;

    public Photo(File location) {
        this.location=location.getAbsolutePath();
        this.lib=new ArrayList<Tag>();
    }
    public Photo(String location) {   //temporary
        this.location=location;
        this.lib=new ArrayList<Tag>();
    }

    public void addTag(Tag temp) {
        for (int i=0;i<lib.size();i++){
            if(lib.get(i).getName().equalsIgnoreCase("location") && temp.getName().equalsIgnoreCase("location")){
                return;
            }
        }
        lib.add(temp);
    }
    public void deleteTag(Tag temp){
        lib.remove(temp);
    }

    public ArrayList<Tag> getLib() {
        return lib;
    }
    public void setLib(ArrayList<Tag>t) {
        lib=t;
    }

    public String getUri() {
        return checkPath;
    }
    public void setUri(String t) {
        checkPath=t;
    }

    public String getLocation() {
        return location;
    }

    public String toString() {
        return location;
    }
}

