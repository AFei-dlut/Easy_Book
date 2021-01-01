package com.example.easy_book.bean;

public class Msg {

    private String msg_a;//a用户
    private String msg_a_nickname;//a用户姓名
    private String msg_b;//b用户
    private String msg_b_nickname;//b用户姓名
    private String content;//消息内容
    private String type;//发送还是接受
    private String timestamp;//消息时间


    public String getMsg_a(){
        return msg_a;
    }

    public void setMsg_a(String msg_a){
        this.msg_a = msg_a;
    }

    public String getMsg_a_nickname(){
        return msg_a_nickname;
    }

    public void setMsg_a_nickname(String msg_a_nickname){
        this.msg_a_nickname = msg_a_nickname;
    }

    public String getMsg_b(){
        return msg_b;
    }

    public void setMsg_b(String msg_b){
        this.msg_b = msg_b;
    }

    public String getMsg_b_nickname(){
        return msg_b_nickname;
    }

    public void setMsg_b_nickname(String msg_b_nickname){
        this.msg_b_nickname = msg_b_nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }



}
