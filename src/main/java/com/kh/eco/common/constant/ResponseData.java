package com.kh.eco.common.constant;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseData {
	
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final Object data; 
    private final LocalDateTime timeStamp;
    
    

}
