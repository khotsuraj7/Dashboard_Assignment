
package com.example.sellerdashboard.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "sellers")
@Data
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String region;
}
