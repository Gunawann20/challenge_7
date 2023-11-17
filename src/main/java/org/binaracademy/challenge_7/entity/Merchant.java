package org.binaracademy.challenge_7.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String name;
    private String location;
    @Column(name = "open")
    private Boolean isOpen;
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<Product> products;
}
