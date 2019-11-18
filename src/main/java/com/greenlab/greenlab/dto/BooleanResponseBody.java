package com.greenlab.greenlab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooleanResponseBody{
    String message;
    boolean bool;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
}