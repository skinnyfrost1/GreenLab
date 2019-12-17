package com.greenlab.greenlab.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiStringRequestBody{
    String courseObjectId;
    List<String> strs;
    
}