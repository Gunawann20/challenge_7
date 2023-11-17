package org.binaracademy.challenge_7.entity.response;

import lombok.Data;

@Data
public class Response <T>{
    private boolean error;
    private String message;
    private T data;
}
