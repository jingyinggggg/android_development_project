package com.example.transportpro;

import android.graphics.drawable.Drawable;

public class NotificationClass {

    int userId;
    String  image;
    String title;
    String type;
    String content;
    int is_read;
    int is_system;


    public NotificationClass(){
        // Default constructor required for Firebase
    }

    public NotificationClass(int userId,String  image, String title, String type, String content, int is_read,int is_system){
        this.userId = userId;
        this.image = image;
        this.title = title;
        this.type = type;
        this.content = content;
        this.is_read = is_read;
        this.is_system = is_system;
    }

    //Getter
    public int getUserId() {
        return userId;
    }
    public String  getImage(){return image;}
    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public String getContent(){return content;}
    public int getIs_read() {
        return is_read;
    }
    public int getIs_system(){return is_system;}

    //Setter

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setImage(String  image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content){this.content = content;}

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public void setIs_system(int is_system) {
        this.is_system = is_system;
    }
}
