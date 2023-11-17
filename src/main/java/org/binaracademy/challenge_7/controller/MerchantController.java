package org.binaracademy.challenge_7.controller;

import org.binaracademy.challenge_7.entity.dto.MerchantDto;
import org.binaracademy.challenge_7.entity.request.MerchantRequest;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MerchantController {

    private final MerchantService merchantService;
    @Autowired
    public MerchantController(MerchantService merchantService){
        this.merchantService = merchantService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(
            value = "/merchant",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> createMerchant(@RequestBody MerchantRequest request){
        return ResponseEntity.ok(merchantService.save(request));
    }

    @GetMapping(
            value = "/merchants",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<MerchantDto>>> getListMerchantOpen(){
        return ResponseEntity.ok(merchantService.merchantListOpen());
    }
}
