package org.binaracademy.challenge_7.repository;

import org.binaracademy.challenge_7.entity.Order;
import org.binaracademy.challenge_7.entity.response.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderByUser_IdAndIsCompleted(Long userId, Boolean isCompleted);
    @Query("select o from Order o where o.isCompleted = false and o.user.id = :userId")
    Order getDataOrderFalseById(@Param("userId") Long userId);
    @Query("select new org.binaracademy.challenge_7.entity.response.OrderResponse(o.id, o.user.id, o.destination, o.time, o.isCompleted) from Order o where o.isCompleted = false and o.user.id =:userId")
    OrderResponse getDataCurrentOrder(@Param("userId") Long userId);
    @Query("select new org.binaracademy.challenge_7.entity.response.OrderResponse(o.id, o.user.id, o.destination, o.time, o.isCompleted) from Order o where o.isCompleted = true and o.user.id =:userId")
    List<OrderResponse> getListHistoryOrder(@Param("userId") Long userId);
}
