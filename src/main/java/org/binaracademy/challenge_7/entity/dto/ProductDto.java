package org.binaracademy.challenge_7.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {

    private Long code;
    private Long merchantCode;
    private String name;
    private Integer price;
}
