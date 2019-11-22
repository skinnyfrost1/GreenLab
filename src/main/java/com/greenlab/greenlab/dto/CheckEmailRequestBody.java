package com.greenlab.greenlab.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;


public class CheckEmailRequestBody {

    @Getter
    @Setter
    @NotBlank(message = "email can't empty!")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}