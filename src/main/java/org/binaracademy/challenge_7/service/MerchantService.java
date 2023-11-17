package org.binaracademy.challenge_7.service;

import org.binaracademy.challenge_7.entity.dto.MerchantDto;
import org.binaracademy.challenge_7.entity.request.MerchantRequest;
import org.binaracademy.challenge_7.entity.response.Response;

import java.util.List;

public interface MerchantService {
    Response<String> save(MerchantRequest merchant);
    Response<String> update(Long merchantCode, boolean isOpen);
    Response<List<MerchantDto>> merchantListOpen();
    Response<List<MerchantDto>> merchantList();
}
