package org.binaracademy.challenge_7.entity.request;

import lombok.Data;

@Data
public class MerchantRequest {
    private String name;
    private String location;
    private Boolean isOpen;
}
