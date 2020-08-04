package com.github.davejoyce.acme.model.validator;

import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OtherProductFormValidator extends AbstractProductFormValidator {

    public OtherProductFormValidator() {
        super();
    }

    @Override
    public boolean supportsProductType(ProductForm productForm) {
        ProductType type;
        try {
            type = productForm.getProductType();
            switch (type) {
                case HOSTING:
                case EMAIL:
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    void validateProductForm(ProductForm productForm, Errors errors) {
        validateDuration(productForm.getDuration(), errors);
    }

}
