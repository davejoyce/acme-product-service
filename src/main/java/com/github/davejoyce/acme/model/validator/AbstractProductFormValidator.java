package com.github.davejoyce.acme.model.validator;

import com.github.davejoyce.acme.model.ProductForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class AbstractProductFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (supportsProductType((ProductForm)target)) {
            validateProductForm((ProductForm)target, errors);
        }
    }

    public abstract boolean supportsProductType(ProductForm productForm);

    abstract void validateProductForm(ProductForm productForm, Errors errors);

    void validateDuration(Integer productDuration, Errors errors) {
        if (null == productDuration) {
            errors.rejectValue("duration", "duration.missing", "Missing required product duration");
            return;
        }
        if (0 >= productDuration) {
            errors.rejectValue("duration", "duration.invalid", "Invalid product duration");
        }
    }

}
