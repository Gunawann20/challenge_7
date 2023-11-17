package org.binaracademy.challenge_7.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id"
    )
    private Order order;
    @ManyToOne
    @JoinColumn(
            name = "product_code",
            referencedColumnName = "code"
    )
    private Product product;
    private Integer quantity;
    @Column(name = "total_price")
    private Long totalPrice;
}
