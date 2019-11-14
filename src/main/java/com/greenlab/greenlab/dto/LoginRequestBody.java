package com.greenlab.greenlab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestBody {

    private String email;
    private String password;

}