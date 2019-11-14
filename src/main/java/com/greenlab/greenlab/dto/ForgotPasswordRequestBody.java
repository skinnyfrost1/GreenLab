package com.greenlab.greenlab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class ForgotPasswordRequestBody{
    private String email;
    private String uid;
    private String password;
}