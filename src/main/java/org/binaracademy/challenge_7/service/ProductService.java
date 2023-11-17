package org.binaracademy.challenge_7.service;

import org.binaracademy.challenge_7.entity.dto.ProductDto;
import org.binaracademy.challenge_7.entity.request.ProductRequest;
import org.binaracademy.challenge_7.entity.response.Response;

import java.util.List;

public interface ProductService {
    Response<ProductDto> save(Long merchantCode, ProductRequest product);
    Response<ProductDto> update(Long productCode, ProductRequest product);
    Response<String> delete(Long productCode);
    Response<List<ProductDto>> listProduct();
}
