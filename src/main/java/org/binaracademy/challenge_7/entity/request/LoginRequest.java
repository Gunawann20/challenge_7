package org.binaracademy.challenge_7.entity.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
