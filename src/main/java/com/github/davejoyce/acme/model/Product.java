package com.github.davejoyce.acme.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    private Date startDate;
    private int durationMonths;

}
