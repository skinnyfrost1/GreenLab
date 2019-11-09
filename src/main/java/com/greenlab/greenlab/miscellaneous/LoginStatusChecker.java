package com.greenlab.greenlab.miscellaneous;

import javax.servlet.http.HttpServletRequest;

public class LoginStatusChecker{
    String email;
    public String isLogin(HttpServletRequest request){
        email = (String) request.getSession().getAttribute("email");

        return email;
    }


}
