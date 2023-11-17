package org.binaracademy.challenge_7.repository;

import org.binaracademy.challenge_7.entity.OrderDetail;
import org.binaracademy.challenge_7.entity.response.OrderDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("select new org.binaracademy.challenge_7.entity.response.OrderDetailResponse(od.order.id, od.product.name, od.quantity, od.totalPrice) from OrderDetail od where od.order.isCompleted = :isCompleted and od.order.user.id = :userId")
    List<OrderDetailResponse> getListOrderDetail(@Param("userId") Long userId, @Param("isCompleted") Boolean isCompleted);
}
