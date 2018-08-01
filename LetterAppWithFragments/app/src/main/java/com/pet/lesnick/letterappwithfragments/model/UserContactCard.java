package com.pet.lesnick.letterappwithfragments.model;

import android.graphics.Bitmap;

public class UserContactCard {
    public UserContactCard(String email, String name, String bitmap)
    {
        this.email = email;
        this.name = name;
        this.image = bitmap;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String email;
    private String image;
    private String name;
}
