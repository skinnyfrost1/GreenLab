package com.greenlab.greenlab.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RequestLabMenuResponseBody {
    String message;
    List<String> labNameList;
    List<String> lab_IdList;
}