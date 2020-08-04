package com.github.davejoyce.acme.model.validator;

import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EDomainProductFormValidator extends DomainProductFormValidator {

    public EDomainProductFormValidator() {
        super(Collections.singletonList(".edu"));
    }

    @Override
    public boolean supportsProductType(ProductForm productForm) {
        try {
            return ProductType.E_DOMAIN == productForm.getProductType();
        } catch (Exception e) {
            return false;
        }
    }

}
