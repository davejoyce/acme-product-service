package com.github.davejoyce.acme.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductForm {

    private String customerName;
    private String productType;
    private String domain;
    private Integer durationMonths;
    private Date startDate;

}
