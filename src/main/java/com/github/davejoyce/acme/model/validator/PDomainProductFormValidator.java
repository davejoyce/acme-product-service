package com.github.davejoyce.acme.model.validator;

import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDomainProductFormValidator extends DomainProductFormValidator {

    public PDomainProductFormValidator() {
        super();
    }

    @Override
    public boolean supportsProductType(ProductForm productForm) {
        try {
            return ProductType.P_DOMAIN == productForm.getProductType();
        } catch (Exception e) {
            return false;
        }
    }

}
