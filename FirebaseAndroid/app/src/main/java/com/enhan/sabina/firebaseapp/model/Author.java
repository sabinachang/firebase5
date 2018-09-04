package com.enhan.sabina.firebaseapp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Author {

    private String email;
    private String name;


    public Author() {

    }

    public Author(String email,String name) {
        this.email = email;
        this.name = name;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
