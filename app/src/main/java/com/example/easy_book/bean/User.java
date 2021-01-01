package com.example.easy_book.bean;

import android.content.Intent;

import org.w3c.dom.Text;

import java.io.Serializable;
//将类的实例永久化序列化即保存

public class User implements Serializable {

    private String username;
    private String password;
    private String sex;
    private String phone;
    private String nickname;
    private String grade;
    private String college;
    private String major;
    private String QQ;
    private String location;
    private byte[] card;
    private int card_identify;//为1则进行过校园卡识别，0则未进行



    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getSex(){
        return  sex;
    }

    public void setSex(String  sex) {
        this.sex = sex;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getNickname() {
         return nickname;
    }

    public void  setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String  getGrade(){
        return grade;
    }

    public void setGrade(String grade){
        this.grade = grade;
    }

    public String  getCollege(){
        return college;
    }

    public void setCollege(String college){
        this.college = college;
    }

    public String  getMajor(){
        return major;
    }

    public void setMajor(String major){
        this.major = major;
    }

    public String getQQ(){
        return QQ;
    }

    public void setQQ(String QQ){
        this.QQ = QQ;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public int getCard_identify(){
        return card_identify;
    }

    public void setCard_identify(int card_identify){
        this.card_identify = card_identify;
    }

    public byte[] getCard(){
        return card;
    }

    public void setCard(byte[] card){
        this.card = card;
    }

}
