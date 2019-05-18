package com.crypto.artist.digitalportrait.Orders.Objects;

public class Order {

    private String status;
    private String date;
    private String description;
    private String image;
    private String email;

    public  Order(){
        //Necessary to use firebase
    }

    public Order(String status, String date, String description, String image, String email) {
        this.status = status;
        this.date = date;
        this.description = description;
        this.image = image;
        this.email = email;
    }

    public Order(String status, String date) {
        this.status = status;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
