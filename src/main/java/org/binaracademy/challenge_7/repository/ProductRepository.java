package org.binaracademy.challenge_7.repository;

import org.binaracademy.challenge_7.entity.Product;
import org.binaracademy.challenge_7.entity.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select new org.binaracademy.challenge_7.entity.dto.ProductDto(p.code, p.merchant.code, p.name, p.price) from Product p where p.merchant.isOpen = true ")
    List<ProductDto> listProductAvailable();
}
