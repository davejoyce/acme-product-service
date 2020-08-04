package com.github.davejoyce.acme.model.validator;

import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import org.springframework.validation.Errors;

import java.util.*;

public class DomainProductFormValidator extends AbstractProductFormValidator {

    protected static final List<String> SUPPORTED_TLDS = Arrays.asList(".com", ".org");

    protected final List<String> supportedTlds = new ArrayList<>();

    public DomainProductFormValidator() {
        this(SUPPORTED_TLDS);
    }

    DomainProductFormValidator(List<String> supportedTlds) {
        this.supportedTlds.addAll(supportedTlds);
    }

    @Override
    public boolean supportsProductType(ProductForm productForm) {
        try {
            return ProductType.DOMAIN == productForm.getProductType();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    void validateProductForm(ProductForm productForm, Errors errors) {
        validateDomain(productForm.getDomain(), errors);
        validateDuration(productForm.getDuration(), errors);
    }

    void validateDomain(String domain, Errors errors) {
        boolean ok = supportedTlds.stream().anyMatch(tld -> domain.toLowerCase().endsWith(tld));
        if (!ok) {
            errors.rejectValue("domain", "domain.invalid", "Invalid domain name");
        }
    }

}
