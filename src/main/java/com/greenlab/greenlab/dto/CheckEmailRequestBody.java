package com.greenlab.greenlab.dto;

import javax.validation.constraints.NotBlank;


public class CheckEmailRequestBody {

    @NotBlank(message = "email can't empty!")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}