package org.binaracademy.challenge_7.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.challenge_7.entity.Merchant;
import org.binaracademy.challenge_7.entity.Product;
import org.binaracademy.challenge_7.entity.User;
import org.binaracademy.challenge_7.entity.dto.ProductDto;
import org.binaracademy.challenge_7.entity.request.ProductRequest;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.repository.MerchantRepository;
import org.binaracademy.challenge_7.repository.ProductRepository;
import org.binaracademy.challenge_7.repository.UserRepository;
import org.binaracademy.challenge_7.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, MerchantRepository merchantRepository, UserRepository userRepository){
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Response<ProductDto> save(Long merchantCode, ProductRequest product) {
        Response<ProductDto> response = new Response<>();
        try {
            Merchant merchant = merchantRepository.findById(merchantCode).orElse(null);
            User userContext = userRepository.findByUsername(getUsernameContext());
            if (merchant != null && Objects.equals(userContext.getId(), merchant.getUser().getId())){
                Product newProduct = new Product();
                newProduct.setName(product.getName());
                newProduct.setMerchant(merchant);
                newProduct.setPrice(product.getPrice());

                merchant.getProducts().add(newProduct);

                merchantRepository.save(merchant);
                response.setError(false);
                response.setMessage("Success");
                response.setData(new ProductDto(newProduct.getCode(), newProduct.getMerchant().getCode(), newProduct.getName(), newProduct.getPrice()));
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData(null);
            }
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+e.getMessage());
            response.setError(true);
            response.setMessage("Failed");
            response.setData(null);
        }
        return response;
    }

    @Transactional
    @Override
    public Response<ProductDto> update(Long productCode, ProductRequest product) {
        Response<ProductDto> response = new Response<>();
        try {
            Product updatedProduct = productRepository.findById(productCode).orElse(null);
            User userContext = userRepository.findByUsername(getUsernameContext());
            if (updatedProduct != null && Objects.equals(updatedProduct.getMerchant().getUser().getId(), userContext.getId())){
                updatedProduct.setName(product.getName());
                updatedProduct.setPrice(product.getPrice());

                productRepository.save(updatedProduct);
                response.setError(false);
                response.setMessage("Success");
                response.setData(new ProductDto(updatedProduct.getCode(), updatedProduct.getMerchant().getCode(), updatedProduct.getName(), updatedProduct.getPrice()));
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData(null);
            }
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+e.getMessage());
            response.setError(true);
            response.setMessage("Failed");
            response.setData(null);
        }
        return response;
    }

    @Transactional
    @Override
    public Response<String> delete(Long productCode) {
        Response<String> response = new Response<>();
        try {
            Product deleteProduct = productRepository.findById(productCode).orElse(null);
            User userContext = userRepository.findByUsername(getUsernameContext());

            if (deleteProduct != null && Objects.equals(deleteProduct.getMerchant().getUser().getId(), userContext.getId())){
                productRepository.delete(deleteProduct);
                response.setError(false);
                response.setMessage("Success");
                response.setData("Berhasil menghapus produk: "+ productCode);
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData("Gagal menghapus produk: "+ productCode);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Gagal menghapus produk: "+ productCode);
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<ProductDto>> listProduct() {
        Response<List<ProductDto>> response = new Response<>();
        try {
            List<ProductDto> productDtoList= productRepository.listProductAvailable();
            response.setError(false);
            response.setMessage("Success");
            response.setData(productDtoList);
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+ e.getMessage());
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
