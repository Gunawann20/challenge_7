package org.binaracademy.challenge_7.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.challenge_7.entity.Merchant;
import org.binaracademy.challenge_7.entity.Role;
import org.binaracademy.challenge_7.entity.User;
import org.binaracademy.challenge_7.entity.dto.MerchantDto;
import org.binaracademy.challenge_7.entity.request.MerchantRequest;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.repository.MerchantRepository;
import org.binaracademy.challenge_7.repository.RoleRepository;
import org.binaracademy.challenge_7.repository.UserRepository;
import org.binaracademy.challenge_7.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ExecutorService executorService;

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository, UserRepository userRepository, RoleRepository roleRepository, ExecutorService executorService){
        this.merchantRepository = merchantRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.executorService = executorService;
    }

    @Transactional
    @Override
    public Response<String> save(MerchantRequest merchant) {
        Response<String> response = new Response<>();
        try {

            CompletableFuture<Role> roleCompletableFuture = CompletableFuture.supplyAsync(() -> roleRepository.findRolesById(2), executorService);

            User userContext = userRepository.findByUsername(getUsernameContext());

            Merchant newMerchant = new Merchant();
            userContext.getRoles().add(roleCompletableFuture.get());
            newMerchant.setUser(userContext);
            newMerchant.setName(merchant.getName());
            newMerchant.setLocation(merchant.getLocation());
            newMerchant.setIsOpen(merchant.getIsOpen());
            newMerchant.setProducts(new ArrayList<>());

            merchantRepository.save(newMerchant);

            response.setError(false);
            response.setMessage("Success");
            response.setData("Create merchant successfully");
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+ e.getMessage());
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Create merchant failed");
        }
        return response;
    }

    @Transactional
    @Override
    public Response<String> update(Long merchantCode, boolean isOpen) {
        Response<String> response = new Response<>();
        User userContext = userRepository.findByUsername(getUsernameContext());
        Merchant merchant = merchantRepository.findById(merchantCode).orElse(null);
        try {
            if (merchant != null && Objects.equals(merchant.getUser().getId(), userContext.getId())){
                merchant.setIsOpen(isOpen);
                merchantRepository.save(merchant);

                response.setError(false);
                response.setMessage("Success");
                response.setData("Update merchant successfully");
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData("merchant with code "+ merchantCode+" not found or you can't have access to this merchant");
            }
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+ e.getMessage());
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Gagal update data merchant: "+ merchantCode);
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<MerchantDto>> merchantListOpen() {
        Response<List<MerchantDto>> response = new Response<>();

        try{
            List<MerchantDto> merchantDtoList = merchantRepository.listMerchantIsOpen();
            response.setError(false);
            response.setMessage("Success");
            response.setData(merchantDtoList);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("failed");
            response.setData(null);
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<MerchantDto>> merchantList() {
        Response<List<MerchantDto>> response = new Response<>();
        User userContext = userRepository.findByUsername(getUsernameContext());
        try{
            List<MerchantDto> merchantDtoList = merchantRepository.listMerchantByUser(userContext.getId());
            response.setError(false);
            response.setMessage("Success");
            response.setData(merchantDtoList);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("failed");
            response.setData(null);
        }
        return response;
    }

    private String getUsernameContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return authentication.getName();
        }else {
            return null;
        }
    }
}
