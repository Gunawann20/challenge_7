package org.binaracademy.challenge_7.repository;

import org.binaracademy.challenge_7.entity.Merchant;
import org.binaracademy.challenge_7.entity.dto.MerchantDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("SELECT NEW org.binaracademy.challenge_7.entity.dto.MerchantDto(m.code, m.user.id, m.name, m.location, m.isOpen) from Merchant m where m.isOpen = true")
    List<MerchantDto> listMerchantIsOpen();

    @Query("select new org.binaracademy.challenge_7.entity.dto.MerchantDto(m.code, m.user.id, m.name, m.location, m.isOpen) from Merchant m where m.user.id = :userId")
    List<MerchantDto> listMerchantByUser(@Param("userId") Long userId);
}
