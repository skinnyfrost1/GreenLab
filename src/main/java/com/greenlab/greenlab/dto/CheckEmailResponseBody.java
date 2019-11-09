package com.greenlab.greenlab.dto;

public class CheckEmailResponseBody {

    private String message;
    private boolean isExist;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean isExist) {
        this.isExist = isExist;
    }
}