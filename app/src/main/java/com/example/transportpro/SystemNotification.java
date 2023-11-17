package com.example.transportpro;

public class SystemNotification {

    String image;
    String title;
    String type;
    String content;
    int is_read;


    public SystemNotification(){
        // Default constructor required for Firebase
    }

    public SystemNotification(String  image, String title, String type, String content, int is_read){
        this.image = image;
        this.title = title;
        this.type = type;
        this.content = content;
        this.is_read = is_read;
    }

    //Getter
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

    //Setter

    public void setImage(String image) {
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

}
