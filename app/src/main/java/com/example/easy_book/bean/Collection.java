package com.example.easy_book.bean;

public class Collection {

    private String uid;

    private Integer pid;

    private String title;

    private String label;

    private Float price;

    private String description;

    private byte[] picture;


    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public Integer getPid(){
        return pid;
    }

    public void setPid(Integer pid){
        this.pid = pid;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public float getPrice(){
        return price;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public byte[] getPicture(){
        return picture;
    }

    public void setPicture(byte[] picture){
        this.picture = picture;
    }




}
