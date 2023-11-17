package org.binaracademy.challenge_7.controller;

import org.binaracademy.challenge_7.entity.dto.ProductDto;
import org.binaracademy.challenge_7.entity.request.ProductRequest;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PreAuthorize("hasAnyRole('MERCHANT')")
    @PostMapping(
            value = "/product/{merchantCode}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<ProductDto>> createProduct(@PathVariable Long merchantCode, @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.save(merchantCode, productRequest));
    }

    @PreAuthorize("hasAnyRole('MERCHANT')")
    @PutMapping(
            value = "/product/{productCode}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<ProductDto>> updateProduct(@PathVariable Long productCode, @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.update(productCode, productRequest));
    }

    @PreAuthorize("hasAnyRole('MERCHANT')")
    @DeleteMapping(
            value = "/product/{productCode}"
    )
    public ResponseEntity<Response<String>> deleteProduct(@PathVariable Long productCode){
        return ResponseEntity.ok(productService.delete(productCode));
    }

    @GetMapping(
            value = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<ProductDto>>> listProduct(){
        return ResponseEntity.ok(productService.listProduct());
    }
}
