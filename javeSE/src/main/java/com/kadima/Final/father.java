package com.kadima.Final;

public class father {

    private  String name = "";
    private String Height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    @Override
    public String toString() {
        return "{姓名："+name+"}";
    }
}
