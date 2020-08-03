package com.github.davejoyce.acme.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    private ProductType type;
    private String domain;
    private LocalDate startDate;
    private int durationMonths;

}
