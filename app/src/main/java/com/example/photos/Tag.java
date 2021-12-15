package com.example.photos;

import java.io.Serializable;

public class Tag implements Serializable {

    private String name;
    private String value;

    public Tag(String name, String value) {
        this.name=name;
        this.value=value;
    }

    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }

    public String returnTag() {
        return name+"="+value;
    }

    public String toString() {
        return name+"="+value;
    }

}

