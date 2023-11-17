package org.binaracademy.challenge_7.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantDto {

    private Long code;
    private Long userId;
    private String name;
    private String location;
    private Boolean isOpen;
}
