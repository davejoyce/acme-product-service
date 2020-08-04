package com.github.davejoyce.acme.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProductForm {

    @NotNull(message = "Customer ID cannot be null")
    @NotEmpty(message = "Missing required customer ID")
    private String customerName;


    @NotNull(message = "Product type cannot be null")
    private ProductType productType;

    @NotNull(message = "Domain cannot be null")
    @NotEmpty(message = "Missing required domain")
    private String domain;

    @NotNull(message = "Missing required product duration")
    @Min(value = 1, message = "Duration must be at least 1")
    private Integer duration;

    private Date startDate = new Date();

}
